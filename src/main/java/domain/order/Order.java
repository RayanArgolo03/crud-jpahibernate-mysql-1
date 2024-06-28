package domain.order;

import domain.client.Client;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.*;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Order {
    @NonFinal
    UUID id;
    Client client;
    LocalDateTime orderDate;
    Set<OrderItem> orderItems;
}
