<template>
  <deliveryCorp-detail :is-edit='false'></deliveryCorp-detail>
</template>

<script>
import deliveryCorpDetail from './components/deliveryCorpDetail'

export default {
  name: 'createForm',
  components: { deliveryCorpDetail }
}
</script>

