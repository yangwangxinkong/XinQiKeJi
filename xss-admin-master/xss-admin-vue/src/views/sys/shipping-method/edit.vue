<template>
  <shippingMethod-detail :is-edit='true'></shippingMethod-detail>
</template>

<script>
import shippingMethodDetail from './components/shippingMethodDetail'

export default {
  name: 'editForm',
  components: { shippingMethodDetail }
}
</script>

