import React, { Component } from 'react';
import { InputNumber, Select, Row, Col } from 'antd';

const { Option } = Select;

class ChemicalInput extends React.Component {
  static getDerivedStateFromProps(nextProps) {
    // Should be a controlled component.
    if ('value' in nextProps) {
      return {
        ...(nextProps.value || {}),
      };
    }
    return null;
  }

  constructor(props) {
    super(props);

    const value = props.value || {};
    const chemicals = props.chemicals || [];
    const checmicalOptions = chemicals.map((chemical) =>
      <Option key={chemical.id} value={chemical.id}>{chemical.chemicalName}</Option>
    );
    this.state = {
      chemicalOptions: checmicalOptions,
      chemicalId: chemicals[0].id || 0,
      chemicalName: chemicals[0].chemicalName || "",
      contaminationValue: value.contaminationValue || 0,
      contaminationType: value.contaminationType || 'soil',
      measuringUnit: value.measuringUnit || 'mg/kg',
      contaminationValueSd: value.contaminationValueSd || 0,
    };
  }

  handleNumberChange = e => {
    const contaminationValue = parseFloat(e || 0, 10);
    if (isNaN(contaminationValue)) {
      return;
    }
    if (!('value' in this.props)) {
      this.setState({ contaminationValue });
    }
    this.triggerChange({ contaminationValue });
  };

  handleSdChange = e => {
    const contaminationValueSd = parseFloat(e || 0, 10);
    if (isNaN(contaminationValueSd)) {
      return;
    }
    if (!('value' in this.props)) {
      this.setState({ contaminationValueSd });
    }
    this.triggerChange({ contaminationValueSd });
  };

  handleContaminationTypeChange = contaminationType => {
    const measuringUnit = contaminationType === "water" ? "mg/ltr" : "mg/kg";
    if (!('value' in this.props)) {
      this.setState({ contaminationType, measuringUnit });
    }
    this.triggerChange({ contaminationType, measuringUnit });
  };

  handleChemicalChange = chemical_id => {
    const chemicalName = this.props.chemicals.find((chemical) => {
      return chemical.id === chemical_id;
    }).chemicalName

    if (!('value' in this.props)) {
      this.setState({
        chemicalId: chemical_id,
        chemicalName: chemicalName
      });
    }
    this.triggerChange({
      chemicalId: chemical_id,
      chemicalName: chemicalName
    });
  };

  triggerChange = changedValue => {
    // Should provide an event to pass value to Form.
    const { onChange } = this.props;
    if (onChange) {
      onChange({
        ...this.state,
        ...changedValue,
      });
    }
  };

  render() {
    const { size } = this.props;
    const { measuringUnit, chemicalOptions, chemicalId, contaminationType, contaminationValue, contaminationValueSd } = this.state;
    return (
      <div>
        <Row>
          <Col span={8}>
            <Select
              value={contaminationType}
              size={size}
              onChange={this.handleContaminationTypeChange}
            >
              <Option value="soil">Soil</Option>
              <Option value="water">Water</Option>
            </Select>
          </Col>
          <Col span={16}>
            <Select
              value={chemicalId}
              size={size}
              style={{ marginLeft: '5%', width : '95%' }}
              onChange={this.handleChemicalChange}
            >
              {chemicalOptions}
            </Select>
          </Col>
        </Row>
        <Row style={{ marginTop : '10px' }}>
          <Col span={7}><h4>Mean</h4></Col>
          <Col>
          <InputNumber
            type="text"
            size={size}
            value={contaminationValue}
            onChange={this.handleNumberChange}
            style={{ width: '30%', marginLeft: '5%', marginRight: '2%' }}
          />
          {measuringUnit}
          </Col>
        </Row>
        <Row style={{ marginTop : '10px' }}>
          <Col span={7}><h4>Standard Deviation</h4></Col>
          <Col>
          <InputNumber
            type="text"
            size={size}
            value={contaminationValueSd}
            onChange={this.handleSdChange}
            style={{ width: '30%', marginLeft: '5%', marginRight: '2%' }}
          />
          {measuringUnit}
          </Col>
        </Row>
      </div>
    );
  }
}

export default ChemicalInput;