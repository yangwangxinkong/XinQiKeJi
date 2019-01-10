<template>
    <div class="tc">
        <ul class="pagination">
            <li  @click="first" :class="{'disabled': index === 1}"><a href="javascript:void(0);">首页</a></li>
            <li  @click="prev" :class="{'disabled': index === 1}"><a href="javascript:void(0);">&laquo;</a></li>
            <li class="more" v-if="showPrevMore"><a href="javascript:void(0);">...</a></li>
            <li v-for="pager in pagers" :key="pager" :class="{'active':index === pager}" @click="go(pager)"><a href="javascript:void(0);">{{ pager }}</a></li>
            <li class="more"  v-if="showNextMore"><a href="javascript:void(0);">...</a></li>
            <li @click="next" :class="{'disabled': index === pages}"><a href="javascript:void(0);">&raquo;</a></li>
            <li  @click="last" :class="{'disabled': index === pages}"> <a href="javascript:void(0);">末页</a></li>
        </ul>
    </div>
</template>

<script type="text/ecmascript-6">
export default {
  components: {},
  props: {
    perPages: {
      type: Number,
      default: 5
    },
    //当前页码
    pageIndex: {
      type: Number,
      default: 1
    },

    //每页显示条数
    pageSize: {
      type: Number,
      default: 10
    },

    //总记录数
    total: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      index: this.pageIndex, //当前页码
      limit: this.pageSize, //每页显示条数
      size: this.total || 1, //总记录数
      showPrevMore: false,
      showNextMore: false
    };
  },
  watch: {
    pageIndex(val) {
      this.index = val || 1;
    },
    pageSize(val) {
      this.limit = val || 10;
    },
    total(val) {
      this.size = val || 1;
    }
  },
  computed: {
    //计算总页码
    pages() {
      return Math.ceil(this.size / this.limit);
    },

    //计算页码，当count等变化时自动计算
    pagers() {
      const array = [];
      const perPages = this.perPages;
      const pageCount = this.pages;
      let current = this.index;
      const _offset = (perPages - 1) / 2;

      const offset = {
        start: current - _offset,
        end: current + _offset
      };

      //-1, 3
      if (offset.start < 1) {
        offset.end = offset.end + (1 - offset.start);
        offset.start = 1;
      }
      if (offset.end > pageCount) {
        offset.start = offset.start - (offset.end - pageCount);
        offset.end = pageCount;
      }
      if (offset.start < 1) offset.start = 1;

      this.showPrevMore = offset.start > 1;
      this.showNextMore = offset.end < pageCount;

      for (let i = offset.start; i <= offset.end; i++) {
        array.push(i);
      }

      return array;
    }
  },
  methods: {
    // 前一页
    prev() {
      if (this.index > 1) {
        this.go(this.index - 1);
      }
    },
    // 后一页
    next() {
      if (this.index < this.pages) {
        this.go(this.index + 1);
      }
    },
    // 第一页
    first() {
      if (this.index !== 1) {
        this.go(1);
      }
    },
    // 最后一页
    last() {
      if (this.index != this.pages) {
        this.go(this.pages);
      }
    },
    //页面跳转
    go(page) {
      if (this.index !== page) {
        this.index = page;
        //父组件通过change方法来接受当前的页码
        this.$emit("change", this.index);
      }
    }
  },
  created() {},
  mounted() {}
};
</script>

<style scoped lang="less">
</style>
