package sample;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sample.entities.Customer;
import sample.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/name")
    public ResponseEntity<?> getNames() {
        List<String> names = new ArrayList<String>();
        names.add("Stephen Curry");
        names.add("Kevin Durant");
        names.add("Klay Thompson");
        Payload payload = new Payload(names);
        return new ResponseEntity<Payload>(payload, HttpStatus.OK);
    }

    @RequestMapping(value = {"/configuration"}, method = RequestMethod.GET)
    public ResponseEntity<?> getConfiguration() {
        Map javaEnvironment = System.getenv();
        return new ResponseEntity<Map>(javaEnvironment, HttpStatus.OK);
    }

    @RequestMapping(value = {"/configuration/{pathVariable}"}, method = RequestMethod.GET)
    public ResponseEntity<?> getConfigurationOf(@PathVariable String pathVariable) {
        String javaEnvironment = System.getenv(pathVariable);
        return new ResponseEntity<String>(javaEnvironment, HttpStatus.OK);
    }

    @RequestMapping(value = {"/redis/{pathVariable}"}, method = RequestMethod.GET)
    public ResponseEntity<?> getFromRedis(@PathVariable String pathVariable) {

        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        logger.info("Redis Connection Hostname=" + jedisConnectionFactory.getHostName() + " password:" + jedisConnectionFactory.getPassword() + " Port:" + jedisConnectionFactory.getPort());

        Object cachedValue = redisTemplate.opsForValue().get(pathVariable);
        KeyValuePair keyValuePair = new KeyValuePair();
        keyValuePair.setKey(pathVariable);
        keyValuePair.setValue(cachedValue);
        return new ResponseEntity<Object>(keyValuePair, HttpStatus.OK);
    }

    @RequestMapping(value = {"/redis"}, method = RequestMethod.PUT)
    public ResponseEntity<?> putValueinRedis(@RequestBody(required = true) KeyValuePair keyValuePair) {
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        logger.info("Redis Connection Hostname=" + jedisConnectionFactory.getHostName() + " password:" + jedisConnectionFactory.getPassword() + " Port:" + jedisConnectionFactory.getPort());
        redisTemplate.opsForValue().set(keyValuePair.getKey(), keyValuePair.getValue());
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @RequestMapping(value = {"/customer/{pathVariable}"}, method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable String pathVariable) {
        Customer customer = customerRepository.findOne(Long.parseLong(pathVariable));
        return new ResponseEntity<Object>(customer, HttpStatus.OK);
    }

    @RequestMapping(value = {"/customer"}, method = RequestMethod.GET)
    public ResponseEntity<?> getAllCustomer() {
        Iterable<Customer> customers = customerRepository.findAll();
        return new ResponseEntity<Object>(customers, HttpStatus.OK);
    }
    @RequestMapping(value = {"/customer"}, method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomer(@RequestBody(required = true) Customer customer) {
        customer = customerRepository.save(customer);
        return new ResponseEntity<Object>(customer, HttpStatus.OK);
    }

}

