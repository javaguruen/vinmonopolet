package no.hamre.polet.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import no.hamre.polet.service.ProductDataService;

@Component
public class GraphQLDataFetchers {
  private static Logger LOG = LoggerFactory.getLogger(GraphQLDataFetchers.class);
  private ProductDataService productService;

  public GraphQLDataFetchers(ProductDataService productService) {
    this.productService = productService;
  }

  public DataFetcher getProdukter() {
    return dataFetchingEnvironment -> {
      String name = dataFetchingEnvironment.getArgument("name");
      LOG.info("Find whisky by name = {}", name);
      return productService.findProducts(name);
    };
  }
}
