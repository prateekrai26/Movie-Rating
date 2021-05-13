package com.prateek.moviecatalogservice.resources;


import com.netflix.discovery.DiscoveryClient;
import com.prateek.moviecatalogservice.models.CatalogItem;
import com.prateek.moviecatalogservice.models.Movie;
import com.prateek.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private DiscoveryClient discoveryClient;
     @Autowired
    private RestTemplate restTemplate;

//     @Autowired
//     private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId)
    {

     //get all the rated movies Ids
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" +
                userId,  UserRating.class);

        //For all movieIds , call Movie Info and get Details

      return ratings.getUserRating().stream().map(rating -> {
          Movie movie= restTemplate.getForObject("http://movie-info-service/movies/" +
                  rating.getMovieId() , Movie.class);
          //put them all together
          return new CatalogItem(movie.getName(),"Desc" , rating.getRating());
      }).collect(Collectors.toList());










   /*     return Collections.singletonList(
          new CatalogItem("Avengers","Test" , 5)
   );

            Movie movie=webClientBuilder.build()

                .get()
                .uri("http://localhost:8082/movies/" + rating.getMovieId())
           .retrieve().bodyToMono(Movie.class).block();*/
    }

}
