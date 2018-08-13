package sample;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    @GetMapping("/get")
    public ResponseEntity<?> getNames() {
        List<String> names = new ArrayList<String>();
        names.add("Stephen Curry");
        names.add("Kevin Durant");
        names.add("Klay Thompson");
        Payload payload = new Payload(names);
        return new ResponseEntity<Payload>(payload, HttpStatus.OK);
    }
    @RequestMapping(value={"/configuration"},method= RequestMethod.GET)
    public ResponseEntity<?>getConfiguration() {
        Map javaEnvironment = System.getenv();
        return new ResponseEntity<Map>(javaEnvironment, HttpStatus.OK);
    }
    @RequestMapping(value={"/configuration/{pathVariable}"},method= RequestMethod.GET)
    public ResponseEntity<?>getConfigurationOf(@PathVariable String pathVariable) {
        String javaEnvironment = System.getenv(pathVariable);
        return new ResponseEntity<String>(javaEnvironment, HttpStatus.OK);
    }
}

