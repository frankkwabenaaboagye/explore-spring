package frank.ecommerceapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @RequestMapping("/about")
    public ResponseEntity<String> about() {
        return ResponseEntity.ok("About: This is the public about page");
    }
}
