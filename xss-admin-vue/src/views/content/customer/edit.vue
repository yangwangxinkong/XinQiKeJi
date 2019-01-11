<template>
  <customer-detail :is-edit='true'></customer-detail>
</template>

<script>
  import CustomerDetail from './components/customerDetail'

  export default {
    name: 'editForm',
    components: { CustomerDetail }
  }
</script>
