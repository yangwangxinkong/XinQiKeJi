<template>
  <paymentMethod-detail :is-edit='false'></paymentMethod-detail>
</template>

<script>
import paymentMethodDetail from './components/paymentMethodDetail'

export default {
  name: 'createForm',
  components: { paymentMethodDetail }
}
</script>

