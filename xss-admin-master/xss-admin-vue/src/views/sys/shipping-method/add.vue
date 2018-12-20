<template>
  <shippingMethod-detail :is-edit='false'></shippingMethod-detail>
</template>

<script>
import shippingMethodDetail from './components/shippingMethodDetail'

export default {
  name: 'createForm',
  components: { shippingMethodDetail }
}
</script>

