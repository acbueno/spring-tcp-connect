package br.com.acbueno.tcp.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TcpClient {

  @Value("${tcp.client.host}")
  private String host;

  @Value("${tcp.client.port}")
  private int port;

  public void start() throws Exception {
    NioSocketConnector connector = new NioSocketConnector();
    connector.getFilterChain().addLast("codec",
        new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
    connector.setHandler(new IoHandlerAdapter() {

      @Override
      public void messageReceived(IoSession session, Object message) {
        String msg = message.toString();
        System.out.println("Received message: " + msg);
      }
    });

    ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
    future.awaitUninterruptibly();
    IoSession session = future.getSession();

    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("Enter message to send (or 'exit' to quit)");
      String input = scanner.next();

      if ("exit".equalsIgnoreCase(input)) {
        break;
      }

      session.write(input);
      System.out.println("Message sent to server");
    }

    connector.dispose();
    scanner.close();
  }

}
