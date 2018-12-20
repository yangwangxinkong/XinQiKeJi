<template>
  <attribute-detail :is-edit='true'></attribute-detail>
</template>

<script>
import AttributeDetail from './components/AttributeDetail'

export default {
  name: 'createForm',
  components: { AttributeDetail }
}
</script>

