import React, { Component } from 'react';
import { PageHeader } from 'antd';
import './SiteList.css';

class SiteList extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="site-home-container">
                <PageHeader title="Sites List" />
            </div>
        );
    }
}

export default SiteList;