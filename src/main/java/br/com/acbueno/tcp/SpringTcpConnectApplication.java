package br.com.acbueno.tcp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import br.com.acbueno.tcp.client.TcpClient;
import br.com.acbueno.tcp.server.TcpServer;

@SpringBootApplication
public class SpringTcpConnectApplication {

  private final TcpServer server;
  private final TcpClient client;

  public SpringTcpConnectApplication(TcpServer server, TcpClient client) {
    this.server = server;
    this.client = client;
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringTcpConnectApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
      server.start();
      client.start();
    };
  }

}
