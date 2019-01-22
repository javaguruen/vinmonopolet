<template>
  <div class="home">
    <h2>{{products.length}} endringer ({{sistEndret}})</h2>
      <b-table striped responsive :items="products" :fields="fields">
    </b-table>
  </div>
</template>

<script>
// @ is an alias to /src
import axios from 'axios'

export default {
  name: 'home',
  data () {
    return {
      fields: ['produsent', 'varenavn', 'volum'],
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
    getLatest: function () {
      axios.get(`/api/v1/products/latest`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.response = response.data
          this.products = response.data
          this.sistEndret = this.products[0].datotid.substring(0, 10)
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
}
</script>
