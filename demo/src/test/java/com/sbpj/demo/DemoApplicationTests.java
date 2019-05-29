package com.sbpj.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * 静态引用，status、content、equalTo函数可用
 */
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)	//引入Spring对JUnit4的支持
@SpringBootTest
//@SpringBootApplicationConfiguration(classes = DemoApplication.class)	//指定Spring Boot的启动类
@AutoConfigureMockMvc
@WebAppConfiguration	//开启Web应用的配置，用于模拟ServletContext
public class DemoApplicationTests {

	/**
	 * 用于模拟调用Controller的接口发起请求，在@Test定义的Demo1测试用例中，
	 * perform函数执行一次请求调用，
	 * accept用于执行接收的数据类型，
	 * andExpect用于判断接口返回的期望值。
	 */
	private MockMvc mvc;

	/**
	 * Before：Junit中定义在测试用例@Test内容执行前预加载的内容，
	 * 这里用来初始化对DemoApplication的模拟。
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new DemoApplication()).build();
	}

	@Test
	public void Demo1() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/demo1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo("Hello battcn")));
	}
}
