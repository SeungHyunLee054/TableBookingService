package com.zerobase.tablebookingservice.model;

import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import com.zerobase.tablebookingservice.persist.entity.StoreEntity;
import lombok.*;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {
    private String name;
    private String address;
    private String description;

    private Double stars;
    private List<String> reviews;

    public static Store fromEntity(StoreEntity storeEntity){
        List<Integer> starList=storeEntity.getBookings().stream()
                .map(BookingEntity::getStars)
                .toList();
        OptionalDouble average = starList.stream()
                .mapToDouble(x -> x).average();

        Double stars;
        if (average.isPresent()){
            stars=average.getAsDouble();
        }else {
            stars=null;
        }

        return Store.builder()
                .name(storeEntity.getName())
                .address(storeEntity.getAddress())
                .description(storeEntity.getDescription())
                .stars(stars)
                .reviews(storeEntity.getBookings().stream()
                        .map(BookingEntity::getReview)
                        .collect(Collectors.toList()))
                .build();
    }
}
