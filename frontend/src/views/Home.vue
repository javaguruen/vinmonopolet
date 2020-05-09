<template>
  <div class="home">
    <h2>{{products.length}} endringer ({{sistEndret}})</h2>
      <b-table striped responsive :items="products" :fields="fields">
  /*             :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc" :sort-compare-options="{ numeric: true, sensitivity: 'base' } "
    *
        <!-- A virtual composite column -->
        <template slot="change" slot-scope="data">
          <font-awesome-icon :icon=findChangeIcon(data.item.priceChangeKr)></font-awesome-icon>
        </template>
        <template slot="price" slot-scope="data">
          {{data.item.price.pris}}
        </template>
      </b-table>

  </div>
</template>

<script>
// @ is an alias to /src
import axios from 'axios'
// import { fasCartPlus } from '@fortawesome/free-solid-svg-icons'

export default {
  name: 'home',
  data () {
    return {
      fields: [
        { key: 'change', label: 'Change' },
        { key: 'priceChangeKr', label: 'Prisendring(kr)', sortable: true, sortByFormatted: true, formatter: (priceChangeKr, key, item) => (priceChangeKr == undefined) ? 0.00 : priceChangeKr.toFixed(2) },
        { key: 'priceChangePercent', label: 'Prisendring(%)', sortable: true, sortByFormatted: true, formatter: (priceChangePersent, key, item) => (priceChangePersent == undefined) ? 0.00 : priceChangePersent.toFixed(2) },
        { key: 'varenavn', label: 'Navn', sortable: true },
        { key: 'price.pris', label: 'Pris', sortable: true, sortByFormatted: true, formatter: (p, key, item) => p.toFixed(2) },
        'volum',
        { key: 'price.literpris', label: 'Literpris', sortable: true, formatter: (p, key, item) => p.toFixed(2) },
        { key: 'produsent', label: 'produsent', sortable: true },
      ],
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
    findChangeIcon: function (priceChangeKr) {
      if (priceChangeKr == undefined || Math.abs(priceChangeKr - 0.0) < 0.1) {
        console.log('plus for ' + priceChangeKr)
        return 'plus'
      } else if (priceChangeKr > 0) {
        console.log('down for ' + priceChangeKr)
        return 'angle-double-up'
      }
      console.log('down for ' + priceChangeKr)
      return 'angle-double-down'
    },
    getLatest: function () {
      axios.get(`/api/v1/products/latest`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.response = response.data
          this.products = response.data
          const dates = this.products.flatMap(prod => prod.prices).map(price => new Date(price.datotid.substring(0, 10)))
          this.sistEndret = new Date(Math.max.apply(null, dates)).toISOString().slice(0, 10)
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}
</script>
