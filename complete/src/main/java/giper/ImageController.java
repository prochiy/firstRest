package giper;

import database.JDBCUtilities;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by prochiy on 8/22/15.
 */
@RestController
public class ImageController {
    @RequestMapping(value = "image", method = RequestMethod.POST)
    public String getImageURL(@RequestBody byte[] image){
        String imageId = JDBCUtilities.getJBDCUtilities().addImage();
        String url = ImageProc.serverPath + imageId + ".jpg";
        ImageProc.write("", image);
        return url;
    }
}
