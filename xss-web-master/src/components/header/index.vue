<template>
  <div class="headerBox">
    <section class="header">
          <!-- <toolbar></toolbar> -->
          <v-nav></v-nav>
      </section>
      <div class="header-top"></div>
  </div>
</template>

<script type="text/ecmascript-6">
import toolbar from "../toolbar/index.vue";
import Nav from "../nav/index.vue";
export default {
  components: {
    "v-nav": Nav,
    toolbar
  },
  props: {},
  data() {
    return {};
  },
  watch: {},
  computed: {},
  methods: {},
  created() {},
  mounted() {}
};
</script>

<style  lang="less">
@import "../../assets/css/sprite.css";
@import "../../common/less/headerfooter.less";
</style>
