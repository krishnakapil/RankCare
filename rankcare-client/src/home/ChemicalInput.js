import React, { Component } from 'react';
import { InputNumber, Select } from 'antd';

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
      chemical_id: chemicals[0].id || 0,
      chemical_name : chemicals[0].chemicalName || "",
      contamination_value: value.contamination_value || 0,
      contamination_type: value.contamination_type || 'soil',
    };
  }

  handleNumberChange = e => {
    const contamination_value = parseFloat(e || 0, 10);
    if (isNaN(contamination_value)) {
      return;
    }
    if (!('value' in this.props)) {
      this.setState({ contamination_value });
    }
    this.triggerChange({ contamination_value });
  };

  handleContaminationTypeChange = contamination_type => {
    if (!('value' in this.props)) {
      this.setState({ contamination_type });
    }
    this.triggerChange({ contamination_type });
  };

  handleChemicalChange = chemical_id => {
    const chemicalName = this.props.chemicals.find((chemical) => {
      return chemical.id === chemical_id;
    }).chemicalName
    console.log("chemical name " + chemical_id + "  " + chemicalName)
    if (!('value' in this.props)) {
      this.setState({ 
        chemical_id : chemical_id,
        chemical_name : chemicalName
      });
    }
    this.triggerChange({ 
      chemical_id : chemical_id,
      chemical_name : chemicalName
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
    const { chemicalOptions, chemical_id, contamination_type, contamination_value } = this.state;
    return (
      <span>
        <Select
          value={contamination_type}
          size={size}
          style={{ width: '22%', marginRight: '3%' }}
          onChange={this.handleContaminationTypeChange}
        >
          <Option value="soil">Soil</Option>
          <Option value="water">Water</Option>
        </Select>
        <Select
          value={chemical_id}
          size={size}
          style={{ width: '42%', marginRight: '3%' }}
          onChange={this.handleChemicalChange}
        >
          {chemicalOptions}
        </Select>
        <InputNumber
          type="text"
          size={size}
          value={contamination_value}
          onChange={this.handleNumberChange}
          style={{ width: this.props.showRemove ? '20%' : '30%', marginRight : this.props.showRemove ? '5%' : '0' }}
        />
      </span>
    );
  }
}

export default ChemicalInput;