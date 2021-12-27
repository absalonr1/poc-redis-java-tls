package aopazo.testredis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * docker build -t simple-consumer-bx .
 * docker tag simple-consumer-bx absalon1000rr/simple-consumer-bx:v2
 * docker push absalon1000rr/simple-consumer-bx:v6
 */
public class RedisTLS {

   public static void main(String[] args) throws Exception{

      String user="xx";
      String pass="xx";
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(4);
      JedisPool pool = new JedisPool(
            config,
            "master.prod-redis-acl-tls-cluster.0wazm6.use1.cache.amazonaws.com", 
             6379,
             5000, 
             user,
             pass,
             true // SSL
             );
      
      try (Jedis con = pool.getResource()) {
         String nomreRegion = con.hget("HASH_REGIONES","2");
         if(nomreRegion!=null) {
            System.out.println("Getting infop from redis!:"+nomreRegion);
         }
         else{
            // otherwise go to your data source	
            Map<String,String> mapaReg = new HashMap<String,String>();
            mapaReg.put("2", "ANTOFA");
            
            //PUT in cache
            con.hmset("HASH_REGIONES", mapaReg);
            // Set TTL
            con.pexpire("HASH_REGIONES", 2592000);
         }
         
      }catch(Exception e){
         e.printStackTrace();
      }
      finally{
         pool.close();
      }
     
   }
}