<template>
  <PageWrapper>
    <Card>
      <Row>
        <Col span="12">
          <Space direction="vertical">
            <InputNumber addon-before="坐标缩放" :value="scale" step="10" @change="scaleChange" />
            <InputNumber
              addon-before="坐标偏移"
              step="10"
              :value="pointOffset"
              @change="pointOffsetChange"
            />
            <InputNumber
              addon-before="图片Y轴偏移"
              step="10"
              :value="imgOffsetY"
              @change="imgOffsetYChange"
            />
            <InputNumber
              addon-before="图片大小"
              step="10"
              :value="imgSize"
              @change="imgSizeChange"
            />
            <InputNumber
              addon-before="坐标范围"
              step="10"
              :value="maxPoint"
              @change="maxPointChange"
            />
          </Space>
          <Row>
            <CardGrid v-for="player in playerInfo" style="width: 20%">
              <Button type="link" @click="() => addSlot(player.playerSlot)">选择</Button>
              {{ player.playerSlot }}
            </CardGrid>
          </Row>
        </Col>
        <Col span="12">
          <Row>
            <Col span="4">
              <Switch
                :checked="isFixTimeRange"
                checked-children="可选择时间范围"
                un-checked-children="固定时间范围"
                @change="isFixTimeRangeChange"
              />
            </Col>
            <Col span="6">
              <InputNumber
                addon-before="固定时间范围"
                step="10"
                :value="timeRange"
                @change="timeRangeChange"
              />
            </Col>
            <Col span="12">
              <Button type="primary" @click="clickStart">开始</Button>
              <Button @click="clickStop">暂停</Button>
              <InputNumber
                addon-before="行进步长"
                step="10"
                :value="autoStep"
                @change="autoStepChange"
              />
            </Col>
          </Row>
          <Row>
            <Col span="20">
              <Slider
                v-model:value="sliderValue"
                range
                @change="sliderChange"
                :max="durationSeconds"
                :marks="marks"
              />
            </Col>
          </Row>
          <HeatmapChart v-bind="config" style="height: 750px; width: 750px" />
        </Col>
      </Row>
    </Card>
  </PageWrapper>
</template>

<script lang="ts" setup>
  import {
    Button,
    Card,
    CardGrid,
    Col,
    InputNumber,
    Row,
    Slider,
    Space,
    Switch,
  } from 'ant-design-vue';
  import { PageWrapper } from '/@/components/Page';
  import { match_data } from './data';
  import { computed, ref, unref, watch } from 'vue';
  import { HeatmapChart } from '@opd/g2plot-vue';
  import map from '/@/assets/graphical.webp';

  // 坐标缩放
  const scale = ref(700);
  // 坐标偏移
  const pointOffset = ref(-200);
  // 图片Y轴偏移
  const imgOffsetY = ref(-700);
  // 图片大小
  const imgSize = ref(700);
  // 坐标范围
  const maxPoint = ref(500);
  // 是否为固定时间范围
  const isFixTimeRange = ref(false);
  // 固定时间范围
  const timeRange = ref(300);
  // 开始时间
  const start = ref(30);
  // 自动进行开关
  const isAuto = ref(false);
  // 行进步长
  const autoStep = ref(10);
  // 滑动组件值范围
  const sliderValue = ref([unref(start), unref(start) + unref(timeRange)]);
  // 时间轴标注
  const marks = ref<Record<number, any>>({
    0: '0',
    300: '5min',
    600: '10mim',
    900: '15mim',
    1200: '20mim',
    1500: '25mim',
    1800: '30mim',
    2100: '35mim',
  });

  const playerSlots = ref([]);

  const sliderChange = (value: number[]) => {
    if (unref(isFixTimeRange)) {
      sliderValue.value = [value[1] - unref(timeRange), value[1]];
    }
  };

  const playerArray = computed(() =>
    match_data.players
      .filter((f) => unref(playerSlots).includes(f.playerSlot))
      .map((p) => p.playbackData.playerUpdatePositionEvents),
  );
  const dataSource = computed(() =>
    unref(playerArray)
      .flat()
      .map((m) => ({
        x: (m.x / 200.0) * unref(scale) + unref(pointOffset),
        y: (m.y / 200.0) * unref(scale) + unref(pointOffset),
        time: m.time,
      }))
      .map((m) => ({
        key: `${m.x},${m.y}`,
        value: 1,
        ...m,
      })),
  );

  const durationSeconds = match_data.durationSeconds;

  const moveData = computed(() => {
    return Object.values(
      unref(dataSource)
        .filter((f) => f.time > unref(sliderValue)[0] && f.time < unref(sliderValue)[1])
        .reduce((group, data) => {
          const { key } = data;
          group[key] = group[key] ? { ...group[key], value: group[key].value + 1 } : data;
          return group;
        }, {}),
    );
  });

  const playerInfo = match_data.players.map((m) => ({
    playerSlot: m.playerSlot,
    heroId: m.heroId,
    steamAccountId: m.steamAccountId,
    name: m.steamAccount.proSteamAccount?.name || m.steamAccount.name,
  }));

  const config = computed(() => ({
    type: 'density',
    xField: 'x',
    yField: 'y',
    colorField: 'value',
    color: '#F51D27-#FA541C-#FF8C12-#FFC838-#FAFFA8-#80FF73-#12CCCC-#1890FF-#6E32C2',
    tooltip: false,
    annotations: [
      {
        style: {
          width: unref(imgSize),
          height: unref(imgSize),
        },
        type: 'image',
        offsetY: unref(imgOffsetY),
        src: map,
      },
    ],
    data: [
      { x: 0, y: 0, time: 0, value: 0 },
      { x: unref(maxPoint), y: unref(maxPoint), time: 0, value: 0 },
      ...unref(moveData),
    ],
  }));

  const addSlot = (slot: number) => {
    playerSlots.value = [...unref(playerSlots), slot];
  };

  watch(isAuto, () => {
    if (unref(isAuto)) {
      setInterval(() => {
        if (unref(isAuto)) {
          if (unref(isFixTimeRange)) {
            sliderValue.value = [unref(sliderValue)[1] - unref(timeRange), unref(sliderValue)[1]];
          }
          sliderValue.value = [unref(sliderValue)[0], unref(sliderValue)[1] + unref(autoStep)];
        }
      }, 100);
    }
  });

  const clickStart = () => (isAuto.value = true);
  const clickStop = () => (isAuto.value = false);

  const scaleChange = (value) => {
    scale.value = value;
  };
  const pointOffsetChange = (value) => {
    pointOffset.value = value;
  };
  const imgOffsetYChange = (value) => {
    imgOffsetY.value = value;
  };
  const imgSizeChange = (value) => {
    imgSize.value = value;
  };
  const maxPointChange = (value) => {
    maxPoint.value = value;
  };
  const isFixTimeRangeChange = (value) => {
    isFixTimeRange.value = value;
  };
  const timeRangeChange = (value) => {
    timeRange.value = value;
  };
  const autoStepChange = (value) => {
    autoStep.value = value;
  };
</script>

<style lang="less" scoped></style>
