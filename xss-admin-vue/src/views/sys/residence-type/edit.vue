<template>
  <residence-type-detail :is-edit='true'></residence-type-detail>
</template>

<script>
import residenceTypeDetail from './components/residenceTypeDetail'

export default {
  name: 'editForm',
  components: { residenceTypeDetail }
}
</script>
