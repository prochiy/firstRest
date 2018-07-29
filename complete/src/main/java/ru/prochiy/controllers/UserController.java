package ru.prochiy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;
import ru.prochiy.main.*;
import ru.prochiy.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prochiy on 8/22/15.
 */


@RestController
public class UserController {

    private static Logger log = Logger.getLogger(User.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Функция обработчик GET запроса по URl /image, в теле запроса должен содержаться массив байт с изображением
     * запрос обрабатывается асинхронно с низким приоритетом
     * @param image массив байтов содержащий изображение
     * @return Абсолютный путь к файлу изображения
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    @RequestMapping(value = "image", method = RequestMethod.POST)
    public String getImageURL(@RequestBody byte[] image) throws ExecutionException, InterruptedException, IOException {
        log.info("/image RequestMethod.POST String getImageURL(@RequestBody byte[] image)" );
        Future<String> stringFuture = getImageURLMulty(image);
        String url = stringFuture.get();
        return url;
    }
    @Async(value = "restLowPriority")
    private Future<String> getImageURLMulty(byte[] image) throws IOException {
        log.info("Многопоточный вызов Future<String> getImageURLMulty(byte[] image)");
        String url = ImageProc.write(image);
        return new AsyncResult<String>(url);
    }

    /**
     * Функция обработчик POST запроса по URL /user
     * запрос обрабатывается асинхронно с низким приоритетом
     * @param user новый пользователь который будет добавлен на сервер
     * @return уникальный индентификатор нового пользователя
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public long createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        log.info("/user RequestMethod.POST long createUser(@RequestBody User user)");
        Future<Long> longFuture = createUserMulty(user);
        Long id = null;
        id = longFuture.get();
        return id;
    }
    @Async(value = "restLowPriority")
    private Future<Long> createUserMulty(User user){
        log.info("Многопоточный вызов Future<Long> createUserMulty(User user)");
        userService.create(user);
        return new AsyncResult<Long>(user.getId());
    }

    /**
     * Функция обрабатывает GET запрос оп URL /user/{id}
     * запрос обрабатывается асихронно с высоким приоритетом
     * @param id уникальный идентификатор пользователя, которого надо найти на сервере
     * @return объект пользователя с заданым id
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") long id) throws ExecutionException, InterruptedException, UserNotFoundException {
        log.info("/user/"+id+" RequestMethod.GET getUser(@PathVariable(\"id\") long id)");
        if(id <= 0)
            throw new IllegalArgumentException("Недопустимое значение параметра запроса");
        Future<User> userFuture = getUserMulty(id);
        User user = userFuture.get();
        return user;
    }
    @Async(value = "restHighPriority")
    private Future<User> getUserMulty(long id) throws UserNotFoundException {
        log.info("Многопоточный вызов Future<User> getUserMulty(long id)");
        User user = userService.findById(id);
        return new AsyncResult<User>(user);
    }

    /**
     * Функция обрабатывет запрос PUT по URL /user/{id} и обновляет статус пользователя
     * запрос выполняется асинхронно с низким приоритетом
     * @param id уникальный идентификатор пользователя
     * @param status новый статус пользователя, true - online, false - offline
     * @return возвращается map с ключами: id - уникальный идентификатор пользователя
     *                                     oldStatus - старый статутс пользователя
     *                                     newStatus - новый статус пользователя
     * @throws UserNotFoundException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Map<String, Object> updateStatus(@PathVariable("id") long id, @RequestParam("status") boolean status) throws UserNotFoundException, ExecutionException, InterruptedException {
        log.info("/user/?id="+id+"&status="+status+" RequestMethod.PUT Map<String, Object> updateStatus(@PathVariable(\"id\") long id, @RequestParam(\"status\") boolean status)");
        if(id <= 0)
            throw new IllegalArgumentException("Недопустимое значение параметра запроса");
        Future<Map<String, Object>> mapFuture = updateSatusMulty(id, status);
        Map<String, Object> stringObjectMap = mapFuture.get();
        return userService.update(id, status);
    }
    @Async(value = "restLowPriority")
    private Future<Map<String, Object>> updateSatusMulty(long id, boolean status) throws UserNotFoundException {
        log.info("Многопоточный вызов Future<Map<String, Object>> updateSatusMulty(long id, boolean status)");
        Map<String, Object> stringObjectMap = userService.update(id, status);
        return new AsyncResult<Map<String, Object>>(stringObjectMap);
    }

    /**
     * Фукция обрабатывает GET запрос по URL /user/statistics?status='???'&timestamp='???'
     * запрос обрабатывается асинхронно с высоким приоритетом
     * @param status статус пользователя
     * @param timestamp дата последниего изменения
     * @return возвращает список карт с следующими ключами: id - уникальный идентификатор пользователя
     *                                                      imageURL - URL картики на сервере
     *                                                      status - статус пользователя
     *                                                      createAt - дата последнего изменения
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/user/statistics", method = RequestMethod.GET)
    public List<Map<String, Object>> getStatistics(@RequestParam(value = "status", required = false) Boolean status,
                                                   @RequestParam(value = "timestamp", required = false) Date timestamp) throws ExecutionException, InterruptedException {
        log.info("user/statistics?status="+status+"&Date="+timestamp+"RequestMethod.GET List<Map<String, Object>> getStatistics(@RequestParam(value = \"status\", required = false) Boolean status,\n" +
                " @RequestParam(value = \"timestamp\", required = false) Date timestamp)");
        System.out.println("Параметры запроса status = " + status + " timestamp = " + timestamp);
        Future<List<Map<String, Object>>> listFuture = getStatisticsMulty(status, timestamp);
        List<Map<String, Object>> mapList = listFuture.get();
        return mapList;
    }
    @Async(value = "restHighPriority")
    public Future<List<Map<String, Object>>> getStatisticsMulty(Boolean status, Date timestamp){
        log.info("Многопоточный вызов Future<List<Map<String, Object>>> getStatisticsMulty(Boolean status, Date timestamp)");
        List<User> userList = userService.findByStatusOrCreatedAt(status, null);
        List<Map<String, Object>> list = new ArrayList<>();
        System.out.println("Коллчество пользователей с заданными параметрами " + userList.size());
        for(User user: userList){
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("imageURL",user.getImageURL());
            map.put("status", user.getCreatedAt());
            map.put("createdAt", user.getCreatedAt());
            list.add(map);
        }
        return new AsyncResult<List<Map<String,Object>>>(list);
    }

    /**
     * Фукнция обработчик исключение UserNotFoundException
     * @param response ответ сервера
     * @param e перехватываемое исключение
     * @throws IOException
     */
    @ExceptionHandler(value = UserNotFoundException.class)
    void handleBadRequests(HttpServletResponse response, Exception e) throws IOException {
        log.log(Level.SEVERE, e.toString());
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Данного пользователя не существует в системе");
    }

    /**
     * Функция обработчик исключения IllegalArgumentException
     * @param response ответ сервера
     * @param e перехватываемое исключение
     * @throws IOException
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    void handleIllegalParametrRequests(HttpServletResponse response, Exception e) throws IOException {
        log.log(Level.SEVERE, e.toString());
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * Функция обработчик исключения FileNotFoundException и IOException
     * @param response ответ сервера
     * @param e перехватываемое исключение
     * @throws IOException
     */
    @ExceptionHandler(value = {FileNotFoundException.class, IOException.class})
    void handleInternalServerError(HttpServletResponse response, Exception e) throws IOException {
        log.log(Level.SEVERE, e.toString());
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ошибка записи файла");

    }

}
