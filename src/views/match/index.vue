<template>
  <PageWrapper>
    <Card>
      <Row>
        <Col span="12">
          <Slider v-model:value="sliderValue" range @change="sliderChange" :max="3600" />
        </Col>
      </Row>
      {{ moveData[0] }}
      <HeatmapChart v-bind="config" style="height: 750px; width: 750px" />
    </Card>
  </PageWrapper>
</template>

<script lang="ts" setup>
  import { Card, Col, Row, Slider } from 'ant-design-vue';
  import { PageWrapper } from '/@/components/Page';
  import { match_data } from './data';
  import { computed, ref, unref } from 'vue';
  import { HeatmapChart } from '@opd/g2plot-vue';
  import map from '/@/assets/graphical.webp';

  const scale = ref(700);
  const start = ref(30);
  const timeRange = ref(5);
  const sliderValue = ref([unref(start), unref(start) + unref(timeRange)]);

  const sliderChange = (value: number[]) => {
    // sliderValue.value = [value[1] - unref(timeRange), value[1]];
  };

  const moveData = computed(() => {
    return Object.values(
      match_data.players
        .map((p) => p.playbackData.playerUpdatePositionEvents)[2]
        .filter((f) => f.time > unref(sliderValue)[0] && f.time < unref(sliderValue)[1])
        .map((m) => ({
          x: (m.x / 200.0) * unref(scale) - 200,
          y: (m.y / 200.0) * unref(scale) - 200,
          time: m.time,
        }))
        .map((m) => ({
          key: `${m.x},${m.y}`,
          value: 1,
          ...m,
        }))
        .reduce((group, data) => {
          const { key } = data;
          group[key] = group[key] ? { ...group[key], value: group[key].value + 1 } : data;
          return group;
        }, {}),
    );
  });

  const config = computed(() => ({
    type: 'density',
    xField: 'x',
    yField: 'y',
    colorField: 'value',
    color: '#F51D27-#FA541C-#FF8C12-#FFC838-#FAFFA8-#80FF73-#12CCCC-#1890FF-#6E32C2',
    annotations: [
      {
        style: {
          width: 700,
          height: 700,
        },
        type: 'image',
        offsetY: -700,
        src: map,
      },
    ],
    data: [
      { x: 0, y: 0, time: 0, value: 0 },
      { x: 500, y: 500, time: 0, value: 0 },
      ...unref(moveData),
    ],
  }));
</script>

<style lang="less" scoped></style>
