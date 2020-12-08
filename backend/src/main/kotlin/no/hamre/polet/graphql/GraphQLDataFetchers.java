package no.hamre.polet.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;

@Component
public class GraphQLDataFetchers {
  private static Logger LOG = LoggerFactory.getLogger(GraphQLDataFetchers.class);

  public DataFetcher getProdukter() {
    return dataFetchingEnvironment -> {
      String name = dataFetchingEnvironment.getArgument("name");
      LOG.info("Find whisky by name = {}", name);
      return GraphQLData.INSTANCE.getProdukt();
    };
  }
}
