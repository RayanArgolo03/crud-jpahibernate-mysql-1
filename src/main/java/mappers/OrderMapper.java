package mappers;


import dtos.output.OrderOutputDTO;
import model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "dateFormatted", expression = "java(order.getFormattedDate())")
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "total", expression = "java(order.getTotal())")
    @Mapping(target = "clientName", expression = "java(order.getClient().getName())")
    OrderOutputDTO orderToOutput(Order order);

}
