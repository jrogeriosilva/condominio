package app.condominio.utils;

import org.springframework.stereotype.Component;

@Component
public class GatewayUtils {
	private String gatewayUrl = "http://localhost:8080";
	
	public String getUrl() {
		return gatewayUrl;
	}
}
