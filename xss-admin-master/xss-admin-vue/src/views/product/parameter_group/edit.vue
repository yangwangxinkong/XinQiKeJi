<template>
  <parameter-group-detail :is-edit='true'></parameter-group-detail>
</template>

<script>
import ParameterGroupDetail from './components/ParameterGroupDetail'

export default {
  name: 'createForm',
  components: { ParameterGroupDetail }
}
</script>

