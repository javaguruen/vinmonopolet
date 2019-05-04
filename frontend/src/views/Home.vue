<template>
  <div class="home">
    <h2>{{products.length}} endringer ({{sistEndret}})</h2>
      <b-table striped responsive :items="products" :fields="fields">
        <!-- A virtual composite column -->
        <template slot="change" slot-scope="data">
          <font-awesome-icon :icon=findChangeIcon(data.item.prices)></font-awesome-icon>
        </template>
      </b-table>

  </div>
</template>

<script>
// @ is an alias to /src
import axios from 'axios'
import {fasCartPlus} from '@fortawesome/free-solid-svg-icons'

export default {
  name: 'home',
  data () {
    return {
      fields: [{ key: 'change', label: 'Change' }, 'produsent', 'varenavn', 'volum'],
      sistEndret: '',
      products: [],
      response: {},
      errors: []
    }
  },
  mounted: function () {
    this.getLatest() // method1 will execute at pageload
  },
  methods: {
    findChangeIcon: function ( prices ) {
      const numPrices = prices.length
      if( numPrices === 0) return ""
      if ( numPrices === 1 ) return "plus"
      if ( prices[0].pris > prices[1].pris)
        return "angle-double-up"
      return "angle-double-down"
    },
    getLatest: function () {
      axios.get(`/api/v1/products/latest`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.response = response.data
          this.products = response.data
          const dates = this.products.flatMap(prod => prod.prices).map( price => new Date(price.datotid.substring(0, 10)) )
          this.sistEndret = new Date(Math.max.apply(null,dates)).toISOString().slice(0,10)
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}
</script>
