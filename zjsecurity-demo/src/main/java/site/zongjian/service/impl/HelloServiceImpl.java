package site.zongjian.service.impl;

import site.zongjian.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author zhailiang
 *
 */
@Service
public class HelloServiceImpl implements HelloService {

	/* (non-Javadoc)
	 * @see site.zongjian.service.HelloService#greeting(java.lang.String)
	 */
	@Override
	public String greeting(String name) {
		System.out.println("greeting");
		return "hello " + name;
	}

}
