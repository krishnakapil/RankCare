import React, { Component } from 'react';
import {
  Chart,
  Geom,
  Axis,
  Tooltip,
  Legend,
} from "bizcharts";
import DataSet from "@antv/data-set";

class RankChart extends Component {
  static defaultProps = {
    onSiteClicked: (siteNameKey) => {}
 }

  constructor(props) {
    super(props);

    this.state = {
      fields: props.fields,
      data: props.data,
      key: props.key,
      value: props.value
    }

    this.handleToolTipClicked = this.handleToolTipClicked.bind(this);
  }


  handleToolTipClicked() {

  }

  render() {
    const data = this.state.data;
    const ds = new DataSet();
    const dv = ds.createView().source(data);

    dv.transform({
      type: "fold",
      fields: this.state.fields,
      key: "X-Axis",
      value: "Tier Value"
    });

    return (
      <div>
        <Chart width={1000} height={500} data={dv} forceFit onPlotClick={evt => {
          this.props.onSiteClicked(evt.data._origin.name);
        }} >
          <Axis name="X-Axis" />
          <Axis name="Tier Value" />
          <Legend />
          <Tooltip
            crosshairs={{
              type: "y"
            }}
          />
          <Geom
            select={true}
            type="interval"
            position="X-Axis*Tier Value"
            color={"name"}
            adjust={[
              {
                type: "dodge",
                marginRatio: 1 / 32
              }
            ]}
          />
        </Chart>
      </div>
    );
  }
}

export default RankChart;