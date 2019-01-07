<template>
  <customer-detail :is-edit='false'></customer-detail>
</template>

<script>
  import CustomerDetail from './components/customerDetail'

  export default {
    name: 'createForm',
    components: { CustomerDetail }
  }
</script>
