package com.huuphucdang.fashionshop.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/img")
@RequiredArgsConstructor
public class CloudinaryController {

    public Cloudinary getCloudinary(){

//        Map config = new HashMap();
//        config.put("cloud_name", "djz6golwu");
//        config.put("api_key", "431586836486845");
//        config.put("api_secret", "yNJllVWkjQVVpNMYaqOMEXoDhcc");
//        config.put("secure", true);
//        return new Cloudinary(config);

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djz6golwu",
                "api_key", "431586836486845",
                "api_secret", "yNJllVWkjQVVpNMYaqOMEXoDhcc"));
        return cloudinary;
    }
    @DeleteMapping("/delete/{id}")
    public void deleteImage(@PathVariable("id") String id){
        try {
            Map options = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );
//            getCloudinary().uploader().destroy(id,options);
            getCloudinary().uploader().destroy(id,options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
