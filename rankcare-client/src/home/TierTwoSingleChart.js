import React, { Component } from 'react';
import {
  Chart,
  Geom,
  Axis,
  Tooltip,
  Legend,
} from "bizcharts";
import DataSet from "@antv/data-set";

class TierTwoSingleChart extends Component {
  constructor(props) {
    super(props);
    const t2Data = props.sitesData[0].t2;
    const fields = Object.keys(t2Data);
    const types = ["CR","NCR"]
    const data = [];

    for(const [index, type] of types.entries()) {
        var typeData = {}
        typeData.name = type
  
        // For Each site
        for(const [index, ageGroup] of fields.entries()) {
          typeData[ageGroup] = t2Data[ageGroup][type];
        }
  
        data.push(typeData)
    }

    this.state = {
      fields : fields,
      data : data
    }
  }

  // Sample data
//   const data = [
//     {
//       name: "NCR",
//       "0-3": 180.9
//     },
//     {
//       name: "CR",
//       "0-3": 18.9
//     }
//   ];

  render() {
    const data = this.state.data;
    const ds = new DataSet();
    const dv = ds.createView().source(data);

    dv.transform({
      type: "fold",
      fields: this.state.fields,
      key: "Age Group",
      value: "Tier Value"
    });

    return (
      <div>
        <Chart width={1000} height={500} data={dv} forceFit>
          <Axis name="Age Group" />
          <Axis name="Tier 2 Value" />
          <Legend />
          <Tooltip
            crosshairs={{
              type: "y"
            }}
          />
          <Geom
            type="interval"
            position="Age Group*Tier Value"
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

export default TierTwoSingleChart;