<template>
  <div class="container">
    <div class='row'>
      <div class="col-12">
        <b-form @submit="doSearch">
          <b-form-group
            id="input-group-1"
            label="Search by name or producer:"
            label-for="input-1">
            <b-form-input
              id="input-1"
              v-model="query"
              type="text"
              required
              placeholder="Enter name or producer"
              autofocus
            ></b-form-input>
          </b-form-group>
          <b-button type="submit" variant="primary">Search</b-button>
        </b-form>

        <h2 v-if="whiskiesFound !== undefined" >Search result ({{whiskies.length}})</h2>
        <b-table v-if="whiskiesFound" striped responsive :items="whiskies" :fields="fields">
          <!-- A virtual composite column -->
          <template slot="varenavn" slot-scope="data">
            <router-link :to="{ name: 'whiskydetails', params: { whiskyid: data.item.id } }">{{ data.item.varenavn }}</router-link>
          </template>
          <template slot="change" slot-scope="data">
            <font-awesome-icon :icon=findChangeIcon(data.item.priceChangeKr)></font-awesome-icon>
          </template>
          <template slot="price" slot-scope="data">
            {{data.item.price.pris}}
          </template>
        </b-table>
      </div>
    </div>
  </div>
</template>
<script>

import axios from 'axios'

export default {
  name: 'search',
  data () {
    return {
      whiskiesFound: undefined,
      query: '',
      whiskies: [],
      fields: [
        // { key: 'id', label: 'id' },
        // { key: 'varenummer', label: 'Varenummer' },
        { key: 'varenavn', label: 'Navn', sortable: true },
        { key: 'produsent', label: 'produsent', sortable: true },
        { key: 'land', label: 'Land', sortable: true },
        { key: 'distrikt', label: 'Distrikt', sortable: true },
        // { key: 'underdistrikt', label: 'Underdistrikt', sortable: true },
        { key: 'produsent', label: 'Produsent', sortable: true }
      ]
    }
  },
  methods: {
    doSearch (evt) {
      evt.preventDefault()
      this.searchByParam(this.query)
    },
    searchByParam (param) {
      // console.log('Query: ' + param)
      axios.get('/api/v1/search', { params: { q: param } })
        .then(response => {
          // JSON responses are automatically parsed.
          this.whiskies = response.data
          if (this.whiskies === undefined) {
            // console.log('this.whiskies undefined')
            this.whiskiesFound = 0
          } else {
            // console.log(this.whiskies.length + ' whiskies found')
            this.whiskiesFound = this.whiskies.length
          }
          // console.log(response.data)
        })
        .catch(e => {
          this.errors.push(e)
        })
    }
  },
  beforeMount () {
    if (this.$route.params.keyword) {
      this.searchByParam(this.$route.params.keyword)
    }
  }
  /*
  beforeRouteUpdate (to, from, next) {
    this.searchByParam(to.params.keyword)
    next()
  }
  */
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
