import React, { Component } from 'react';
import './App.css';
import {
  Route,
  withRouter,
  Switch,
  Redirect
} from 'react-router-dom';

import { getCurrentUser } from '../util/APIUtils';
import { ACCESS_TOKEN } from '../constants';

import Home from '../home/Home';
import Login from '../user/login/Login';
// import Signup from '../user/signup/Signup';
import AppHeader from '../common/AppHeader';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import UserList from '../home/UserList';
import ToxicityData from '../home/ToxicityData';
import ConsumptionData from '../home/ConsumptionData';
import SiteData from '../home/SiteData'
import SiteDetails from '../home/SiteDetails'
import CompareSites from '../home/CompareSites'

import { Layout, notification } from 'antd';
const { Content, Footer } = Layout;

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
      isAuthenticated: localStorage.getItem(ACCESS_TOKEN) != null,
      isLoading: false
    }
    this.handleLogout = this.handleLogout.bind(this);
    this.loadCurrentUser = this.loadCurrentUser.bind(this);
    this.handleLogin = this.handleLogin.bind(this);
    this.handleDetails = this.handleDetails.bind(this);

    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });
  }

  loadCurrentUser() {
    this.setState({
      isLoading: true
    });
    getCurrentUser()
      .then(response => {
        this.setState({
          currentUser: response,
          isAuthenticated: true,
          isLoading: false
        });
      }).catch(error => {
        this.setState({
          isLoading: false,
          isAuthenticated: false
        });
      });
  }

  componentDidMount() {
    this.loadCurrentUser();
  }

  handleLogout(redirectTo = "/login", notificationType = "success", description = "You're successfully logged out.") {
    localStorage.removeItem(ACCESS_TOKEN);

    this.setState({
      currentUser: null,
      isAuthenticated: false
    });

    this.props.history.push(redirectTo);

    notification[notificationType]({
      message: 'rankCare',
      description: description,
    });
  }

  handleLogin() {
    notification.success({
      message: 'rankCare',
      description: "You're successfully logged in.",
    });
    this.loadCurrentUser();
    this.props.history.push("/");
  }

  handleDetails(sites) {

  }

  render() {
    if (this.state.isLoading) {
      return <LoadingIndicator />
    }
    return (
      <Layout className="app-container">
        <AppHeader isAuthenticated={this.state.isAuthenticated}
          currentUser={this.state.currentUser}
          onLogout={this.handleLogout} />

        <Content className="app-content">
          <div className="test-container">
            <Switch>
              <Route exact path="/"
                render={(props) => this.state.isAuthenticated ? <Home isAuthenticated={this.state.isAuthenticated}
                  currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/login" />}>
              </Route>
              <Route path="/login"
                render={(props) => <Login onLogin={this.handleLogin} {...props} />}></Route>
              {/* <Route path="/signup" component={Signup}></Route> */}
              <Route exact path="/manage-users"
                render={(props) => this.state.isAuthenticated && this.state.currentUser && this.state.currentUser.isAdmin ?
                  <UserList currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/" />}>
              </Route>
              <Route exact path="/toxicity"
                render={(props) => this.state.isAuthenticated && this.state.currentUser ?
                  <ToxicityData currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/" />}>
              </Route>
              <Route exact path="/consumption"
                render={(props) => this.state.isAuthenticated && this.state.currentUser ?
                  <ConsumptionData currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/" />}>
              </Route>
              <Route exact path="/sites"
                render={(props) => this.state.isAuthenticated && this.state.currentUser ?
                  <SiteData onDetails={this.handleDetails} currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/" />}>
              </Route>
              <Route exact path="/site-details"
                render={(props) => this.state.isAuthenticated && this.state.currentUser ?
                  <SiteDetails currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/" />}>
              </Route>
              <Route exact path="/site-compare"
                render={(props) => this.state.isAuthenticated && this.state.currentUser ?
                  <CompareSites currentUser={this.state.currentUser} {...props} /> :
                  <Redirect to="/" />}>
              </Route>
              <Route component={NotFound}></Route>
            </Switch>
          </div>
        </Content>
        <Footer style={{ textAlign: 'center' }}>rankCare ©2019 CRC CARE Ptv Ltd. All Rights Reserved</Footer>
      </Layout>
    );
  }
}

export default withRouter(App);