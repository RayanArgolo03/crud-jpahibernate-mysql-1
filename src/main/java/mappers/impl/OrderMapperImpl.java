package mappers.impl;

import dtos.output.OrderOutputDTO;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

import mappers.interfaces.OrderMapper;
import model.order.Order;
import model.order.OrderItem;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T14:47:59-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderOutputDTO orderToOutput(Order order) {
        if ( order == null ) {
            return null;
        }

        Set<OrderItem> items = null;

        Set<OrderItem> set = order.getOrderItems();
        if ( set != null ) {
            items = new LinkedHashSet<OrderItem>( set );
        }

        String dateFormatted = order.getFormattedDate();
        String total = order.getTotal();

        OrderOutputDTO orderOutputDTO = new OrderOutputDTO( dateFormatted, items, total );

        return orderOutputDTO;
    }
}
