import React, { Component } from 'react';
import {
  G2,
  Chart,
  Geom,
  Axis,
  Tooltip,
  Coord,
  Label,
  Legend,
  View,
  Guide,
  Shape,
  Facet,
  Util
} from "bizcharts";
import DataSet from "@antv/data-set";

class TierOneChart extends Component {
  constructor(props) {
    super(props);
    const sitesData = props.sitesData
    const fields = sitesData.map((site) => site.siteName);
    const types = ["Water","Soil"];
    const data = [];

    for(const [index, type] of types.entries()) {
      var typeData = {}
      typeData.name = type

      // For Each site
      for(const [index, site] of sitesData.entries()) {
        typeData[site.siteName] = site.t1[type]
      }

      data.push(typeData)
    }

    this.state = {
      fields : fields,
      data : data
    }
  }

  // Sample data
  // const data = [
    //   {
    //     name: "Water",
    //     "New Site": 180.9
    //   },
    //   {
    //     name: "Soil",
    //     "New Site": 12.4
    //   }
    // ];

  render() {
    const data = this.state.data;
    const ds = new DataSet();
    const dv = ds.createView().source(data);

    // Specify Options
    const cols = {
    value: { alias: 'Calculated Values' },
    siteName: { alias: 'Site Name' }
  };

    dv.transform({
      type: "fold",
      fields: this.state.fields,
      key: "Site Name",
      value: "Tier Value"
    });
    return (
      <div>
        <Chart width={1000} height={400} data={dv} forceFit>
          <Axis name="Site Name" />
          <Axis name="Tier 1 Value" />
          <Legend />
          <Tooltip
            crosshairs={{
              type: "y"
            }}
          />
          <Geom
            type="interval"
            position="Site Name*Tier Value"
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

export default TierOneChart;