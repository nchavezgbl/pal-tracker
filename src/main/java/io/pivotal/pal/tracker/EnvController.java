package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvController {

  private final String port;
  private final String limit;
  private final String index;
  private final String address;

  public EnvController(
      @Value("${port:NOT SET}") String port,
      @Value("${memory.limit:NOT SET}") String limit,
      @Value("${cf.instance.index:NOT SET}") String index,
      @Value("${cf.instance.addr:NOT SET}") String address){

    this.port = port;
    this.limit = limit;
    this.index = index;
    this.address = address;
  }
  @GetMapping("/env")
  public Map<String, String> getEnv(){
    Map<String, String> envMap = new HashMap<>();
    envMap.put("PORT", this.port);
    envMap.put("MEMORY_LIMIT", this.limit);
    envMap.put("CF_INSTANCE_INDEX", this.index);
    envMap.put("CF_INSTANCE_ADDR", this.address);
    return envMap;
  }

}
