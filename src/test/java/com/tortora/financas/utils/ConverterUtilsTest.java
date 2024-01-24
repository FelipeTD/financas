package com.tortora.financas.utils;

import com.tortora.financas.model.Employee;
import com.tortora.financas.model.pojo.EmployeePOJO;
import com.tortora.financas.model.pojo.EmployeesPOJO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ConverterUtilsTest {

    @MockBean
    private HttpUtils httpUtils;

    @Autowired
    private ConverterUtils converterUtils;

    @Test
    void pojoTest() {
        EmployeePOJO pojo = new EmployeePOJO(null, null, null);
        pojo.setFirstName("Filipe");
        pojo.setLastName("Tortora");
        pojo.setRole("Programador");
        List<EmployeePOJO> listPOJO = new ArrayList<>();
        listPOJO.add(pojo);
        EmployeesPOJO pojos = new EmployeesPOJO();
        pojos.setEmployeesPOJO(listPOJO);

        Assertions.assertEquals(1, pojos.getEmployeesPOJO().size());
    }

    @Test
    void convertJsonEmployeesTest() {
        HttpURLConnection mockHttpURLConnection = mock(HttpURLConnection.class);
        String url = "http://teste.com.br";
        String output = "[{\"firstName\":\"firstName 1\",\"lastName\":\"lastName 1\",\"role\":\"role 1\",\"id\":\"1\"}]";

        when(httpUtils.get(url)).thenReturn(mockHttpURLConnection);
        when(httpUtils.reader(mockHttpURLConnection)).thenReturn(new StringBuilder(output));

        List<Employee> employees = converterUtils.convertJsonEmployees(url);

        Assertions.assertEquals(1, employees.size());
    }

    @Test
    void convertXMLEmployeesException() {
        Assertions.assertThrows(RuntimeException.class, () -> converterUtils.convertXMLEmployees(null));
    }

}
