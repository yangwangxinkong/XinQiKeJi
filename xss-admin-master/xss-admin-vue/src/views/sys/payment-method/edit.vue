<template>
  <paymentMethod-detail :is-edit='true'></paymentMethod-detail>
</template>

<script>
import paymentMethodDetail from './components/paymentMethodDetail'

export default {
  name: 'editForm',
  components: { paymentMethodDetail }
}
</script>

