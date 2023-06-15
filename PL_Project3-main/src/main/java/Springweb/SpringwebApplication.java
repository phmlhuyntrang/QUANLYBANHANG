package Springweb;


import Springweb.entity.Customers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringwebApplication.class, args);                
	}
        
//        @Bean(name = "USER_BEAN")
//        public Customers setCustomer()
//        {
//            Customers c = new Customers();
//            c.setEmail("thuyngocmaithyy@gmail.com");
//            c.setPassword("a");
//            return c;
//        }
}
