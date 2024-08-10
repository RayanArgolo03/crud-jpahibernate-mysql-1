package utils;

import enums.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.order.Product;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GetMockProductsUtils {
    public static Set<Product> getMockProducts() {
        return Set.of(
                new Product(null, "Banana", new BigDecimal("1.00"), Set.of(Category.FOODS), null, null),
                new Product(null, "Strawberry", new BigDecimal("2.00"), Set.of(Category.FOODS), null, null),
                new Product(null, "Noodles", new BigDecimal("10.50"), Set.of(Category.FOODS), null, null),
                new Product(null, "Notebook", new BigDecimal("2800.15"), Set.of(Category.ELETRONICS), null, null),
                new Product(null, "Fridge", new BigDecimal("1500.15"), Set.of(Category.ELETRONICS), null, null)
        );
    }

}
