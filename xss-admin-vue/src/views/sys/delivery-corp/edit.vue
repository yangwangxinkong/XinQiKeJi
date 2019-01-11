<template>
  <deliveryCorp-detail :is-edit='true'></deliveryCorp-detail>
</template>

<script>
import deliveryCorpDetail from './components/deliveryCorpDetail'

export default {
  name: 'editForm',
  components: { deliveryCorpDetail }
}
</script>

