package sample;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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

    @RequestMapping(value={"/redis/{pathVariable}"},method= RequestMethod.GET)
    public ResponseEntity<?>getFromRedis(@PathVariable String pathVariable) {
        Object cachedValue = redisTemplate.opsForValue().get(pathVariable);
        KeyValuePair keyValuePair = new KeyValuePair();
        keyValuePair.setKey(pathVariable);
        keyValuePair.setValue((String)cachedValue);
        return new ResponseEntity<Object>(keyValuePair, HttpStatus.OK);
    }

    @RequestMapping(value={"/redis"},method= RequestMethod.PUT)
    public ResponseEntity<?>putValueinRedis(@RequestBody(required = true) KeyValuePair keyValuePair) {
        redisTemplate.opsForValue().set(keyValuePair.getKey(),keyValuePair.getValue());
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}

