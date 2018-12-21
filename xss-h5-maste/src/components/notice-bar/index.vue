<template>
  <div
    v-show="showNoticeBar"
    class="notice-bar"
    :style="barStyle"
    @click="$emit('click')"
  >
    <div v-if="leftIcon" :class="'left-icon notice-bar__left-icon'">
      <img :src="leftIcon" >
    </div>
    <div :class="'wrap notice-bar__wrap'" ref="wrap">
      <div
        ref="content"
        class="notice-bar__content "
        :class="[animationClass]"
        :style="contentStyle"
        @animationend="onAnimationEnd"
        @webkitAnimationEnd="onAnimationEnd"
      >
        <slot>{{ text }}</slot>
      </div>
    </div>
   
  </div>
</template>

<script>
export default {
  name: 'notice-bar',
  props: {
    text: String,
    color: String,
    leftIcon: String,
    background: String,
    delay: {
      type: [String, Number],
      default: 1
    },
    scrollable: {
      type: Boolean,
      default: true
    },
    speed: {
      type: Number,
      default: 50
    }
  },
  data() {
    return {
      wrapWidth: 0,
      firstRound: true,
      duration: 0,
      offsetWidth: 0,
      showNoticeBar: true,
      animationClass:''
    };
  },
  computed: {
    iconName() {
      return this.mode === 'closeable' ? 'close' : this.mode === 'link' ? 'arrow' : '';
    },
    barStyle() {
      return {
        color: this.color,
        background: this.background
      };
    },
    contentStyle() {
      return {
        paddingLeft: this.firstRound ? 0 : this.wrapWidth + 'px',
        animationDelay: (this.firstRound ? this.delay : 0) + 's',
        animationDuration: this.duration + 's'
      };
    }
  },
  watch: {
    text: {
      handler() {
        this.$nextTick(() => {
          const { wrap, content } = this.$refs;
          if (!wrap || !content) {
            return;
          }
          const wrapWidth = wrap.getBoundingClientRect().width;
          const offsetWidth = content.getBoundingClientRect().width;
          if (this.scrollable && offsetWidth > wrapWidth) {
            this.wrapWidth = wrapWidth;
            this.offsetWidth = offsetWidth;
            this.duration = offsetWidth / this.speed;
            this.animationClass = 'notice-bar__play';
          }
        });
      },
      immediate: true
    }
  },
  methods: {
    onAnimationEnd() {
      this.firstRound = false;
      this.$nextTick(() => {
        this.duration = (this.offsetWidth + this.wrapWidth) / this.speed;
        this.animationClass = 'notice-play--infinite';
      });
    }
  }
};
</script>
<style>
.notice-bar {
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  color: #f85;
  padding: 9px 15px;
  font-size: 12px;
  line-height: 1.5;
  position: relative;
  background-color: #fff7cc;
}
.notice-bar--withicon {
    position: relative;
    padding-right: 30px;
  }
.notice-bar__left-icon {
    height: 18px;
    min-width: 20px;
    padding-top: 1px;
    box-sizing: border-box;
  }
.notice-bar__left-icon img {
      width: 16px;
      height: 16px;
    }
.notice-bar__right-icon {
    top: 10px;
    right: 10px;
    position: absolute;
    font-size: 15px;
    line-height: 1;
  }
.notice-bar__wrap {
    -webkit-box-flex: 1;
    -webkit-flex: 1;
            flex: 1;
    height: 18px;
    overflow: hidden;
    position: relative;
  }
.notice-bar__content {
    position: absolute;
    white-space: nowrap;
  }
.notice-bar__play {
    -webkit-animation: notice-bar-play linear both;
            animation: notice-bar-play linear both;
  }
.notice-bar__play--infinite {
    -webkit-animation: notice-bar-play-infinite linear infinite both;
            animation: notice-bar-play-infinite linear infinite both;
  }
/**
 * Declare two same keyframes
 * In case that some mobile browsers can continue animation when className changed
 */
@-webkit-keyframes notice-bar-play {
  to { -webkit-transform: translate3d(-100%, 0, 0); transform: translate3d(-100%, 0, 0); }
}
@keyframes notice-bar-play {
  to { -webkit-transform: translate3d(-100%, 0, 0); transform: translate3d(-100%, 0, 0); }
}
@-webkit-keyframes notice-bar-play-infinite {
  to { -webkit-transform: translate3d(-100%, 0, 0); transform: translate3d(-100%, 0, 0); }
}
@keyframes notice-bar-play-infinite {
  to { -webkit-transform: translate3d(-100%, 0, 0); transform: translate3d(-100%, 0, 0); }
}
</style>
