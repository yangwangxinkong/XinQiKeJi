<template>
  <div class="page">
    <!-- 明细 -->
    <div class="container">
      <div class="bg_f mt10" style="padding: 15px;">
        <p class="f14 tc">订单金额(元)</p>
        <p class="f20 tc amount_color">{{quotationAmount | money}}</p>
        <flexbox class="flexbox-list">
          <flexbox-item class="tc" v-for="item in amountList" :key="item.label">
            <span>{{item.label}}</span>
            <br>
            <span class="amount_color">{{item.value | money}}</span>
          </flexbox-item>
        </flexbox>
      </div>
      <div class="pay-box">
        <p class="f14 tc mt10 grey_be padding-15 bg_f">缴纳月份：{{dateTimes}}</p>
        <group class="amount_color detail-datatime pr">
          <x-icon
            class="fl mt10 mb10 pa left-icon"
            :class="[currentIndex==0?'':'on']"
            size="20"
            type="ios-arrow-left"
            @click="handelLastMonth(datetime)"
          ></x-icon>
          <!-- 根据返回的时间戳转换日期 -->
          <div class="tc" style="line-height:40px;">
            <span>{{datetimeText}}</span>
            <!-- <span v-for="(item,index) in dataRangeList" :key="index" v-if="index==currentIndex">{{item.datetime|parseTime1}}</span> -->
          </div>
          <x-icon
            class="fr mt10 mb10 pa right-icon"
            :class="[currentIndex+1==monthCount?'':'on']"
            size="20"
            type="ios-arrow-right"
            @click="handelNextMonth(datetime)"
          ></x-icon>
          <!-- <datetime class="tc f14" title="" format='YYYY-MM' :max-year="2019" :min-year="2018"   v-model="datetime" @on-change="change" @on-cancel="log('cancel')" @on-hide="log('hide', $event)"></datetime> -->
        </group>
        <x-table :cell-bordered="false" :content-bordered="false" style="background-color:#fff;">
          <thead>
            <tr>
              <th style="color:#333333;font-weight: bold; width:25%;">项目</th>
              <th style="color:#333333;font-weight: bold; width:25%;">基数</th>
              <th style="color:#333333;font-weight: bold; width:25%;">个人</th>
              <th style="color:#333333;font-weight: bold; width:25%;">公司</th>
            </tr>
          </thead>
          <tbody>
            <tr
              :class="item.project === '总计' ? 'amount_color':''"
              v-for="item in lists"
              :key="item.id"
            >
              <td>{{item.project}}</td>
              <td>{{item.cardinalNum | money}}</td>
              <td>{{item.personalNum | money}}</td>
              <td>{{item.companyNum | money}}</td>
            </tr>
          </tbody>
        </x-table>
      </div>
    </div>
  </div>
</template>
<script>
import { Flexbox, FlexboxItem, XTable, Group, Datetime } from "vux";
import { get, post } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
import { money } from "@/utils/utils";

export default {
  components: { Flexbox, FlexboxItem, XTable, Group, Datetime },
  data() {
    return {
      id: this.$route.query && this.$route.query.id,
      amountList: [],
      dateTimes: "2018-09至2019-08",
      lists: [],
      starttime: undefined,
      endtime: undefined,
      monthCount: undefined,
      quotationAmount: undefined,
      quotation: undefined,
      datetime: "2018-09",
      datetimeText: "2018-09",
      dataRangeList: [
        {
          id: 0,
          datetime: "1530374400"
        },
        {
          id: 2,
          datetime: "1533052800"
        },
        {
          id: 3,
          datetime: "1535731200"
        },
        {
          id: 4,
          datetime: "1538323200"
        }
      ],
      currentIndex: 0,
      minId: "1530374400",
      maxId: "1538323200",
      flag: false
    };
  },
  methods: {
    initData() {
      // 选择信息获取
      if (this.id != null) {
        get(url.quotationItemList, { id: this.id })
          .then(response => {
            if (response.data.result == "00000000") {
              this.quotation = response.data.data;
              this.lists = response.data.data.quotationItem;
              this.amountList = response.data.data.amountList;
              this.monthCount = response.data.data.monthCount;
              this.starttime = response.data.data.startDate;
              this.changeDate(response.data.data.startDate);

              this.endtime = response.data.data.endDate;
              this.quotationAmount = response.data.data.amount;

              this.dateTimes =
                response.data.data.startDate +
                "至" +
                response.data.data.endDate;
              this.datetime = response.data.data.startDate;
            }
          })
          .catch(e => {
            console.log(e);
          });
      }
    },
    hideFun() {},
    change() {},
    handelLastMonth(val) {
      // console.log(this.currentIndex);
      if (this.currentIndex === 0) return;
      this.currentIndex--;
      this.addMonth(val, -1);
      // this.dataRangeList.forEach((item, index) => {
      //   if (item.datetime == val) {
      //     if (index == this.currentIndex) {
      //       this.flag=false
      //     }
      //   }
      // });
    },
    handelNextMonth(val) {
      //if (flag) return;
      if (this.currentIndex + 1 === this.monthCount) {
        //flag = true;
        return;
      }
      this.currentIndex++;
      this.addMonth(val, 1);
      // this.dataRangeList.forEach((item, index) => {
      //   if (item.datetime == val) {
      //     if (index == this.currentIndex) {
      //       this.flag = true;
      //     }
      //   }
      // });
    },
    addMonth(date, num) {
      num = parseInt(num);
      var sDate = new Date(date + "-01");

      var sYear = sDate.getFullYear();
      var sMonth = sDate.getMonth() + 1;
      var sDay = sDate.getDate();

      var eYear = sYear;
      var eMonth = sMonth + num;
      var eDay = sDay;
      if (eMonth > 12) {
        eYear++;
        eMonth -= 12;
      } else if (eMonth < 1) {
        eYear--;
        eMonth += 12;
      }

      this.datetime = eYear + "-" + (eMonth < 10 ? "0" + eMonth : eMonth);
      this.datetimeText =
        eYear + "年" + (eMonth < 10 ? "0" + eMonth : eMonth) + "月";
    },
    changeDate(date) {
      //var sDate = new Date(date + "-01");
      var arr = date.split("-");
      if (arr.length >= 2) {
        this.datetimeText = arr[0] + "年" + arr[1] + "月";
      }
    },
    log(str1, str2) {
      console.log(str1, str2);
    },
    title() {}
  },
  created() {
    // 是否登录
    let member = storage.get("member");
    if (member == null && member == undefined) {
      this.$router.push({
        path: "/login/index",
        query: { redirect: this.$route.fullPath }
      });
    }

    this.initData();
  }
};
</script>
<style>
.vux-x-icon.on {
  fill: #e60014;
}
</style>

<style scope lang="less">
.detail-datatime {
  height: 40px;
  line-height: 40px;
  .left-icon {
    left: 10px;
    top: 0;
  }
  .right-icon {
    right: 10px;
    top: 0;
  }
}
.pay-box{
  >p{
    height: 35px;
    line-height: 35px;
  }
}
</style>


