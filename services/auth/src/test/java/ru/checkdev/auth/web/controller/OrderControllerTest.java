package ru.checkdev.auth.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.checkdev.auth.domain.Order;
import ru.checkdev.auth.service.OrderService;

import java.util.Collections;
import java.util.List;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    private Order order;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        order = new Order();
        order.setId(1);
        order.setEmail("www.sss");
        order.setUsername("Test");
        order.setArchive(false);
    }

    @Test
    public void testSaveOrder() throws Exception {

        doNothing().when(orderService).save(any(Order.class));

        mockMvc.perform(post("/order/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(orderService, times(1)).save(any(Order.class));
    }

    @Test
    public void testArchive() throws Exception {
        doAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setArchive(true);
            return null;
        }).when(orderService).toArchive(any(Order.class));

        mockMvc.perform(put("/order/archive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(jsonPath("$.archive").value(true));

        verify(orderService).toArchive(order);
    }

    @Test
    public void testGetOrders() throws Exception {
        List<Order> orders = Collections.singletonList(new Order());

        when(orderService.findByType(anyString())).thenReturn(orders);

        mockMvc.perform(get("/order/list")
                        .param("type", "fresh"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(-1)); // Проверьте нужные поля

        verify(orderService, times(1)).findByType("fresh");
    }
}