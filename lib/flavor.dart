import 'package:flutter/material.dart';

enum Flavor { dev, brandA, brandB }

class FlavorConfig {
  final Flavor flavor;
  final String name;
  final String apiUrl;
  final bool featureX;
  final Color primaryColor;
  final String assetPrefix;
  static FlavorConfig? _instance;

  FlavorConfig._({
    required this.flavor,
    required this.name,
    required this.apiUrl,
    required this.featureX,
    required this.primaryColor,
    required this.assetPrefix,
  }) {
    _instance = this;
  }

  factory FlavorConfig.fromEnvironment() {
    const f = String.fromEnvironment('FLAVOR', defaultValue: 'dev');
    const url = String.fromEnvironment('API_URL', defaultValue: '');
    const fx = bool.fromEnvironment('FEATURE_X_ENABLED', defaultValue: false);
    const c = String.fromEnvironment('COLOR', defaultValue: '0xFF00000');
    switch (f) {
      case 'brandA':
        return FlavorConfig._(
          flavor: Flavor.brandA,
          name: 'BrandA',
          apiUrl: url,
          primaryColor: Color(int.parse(c)),
          featureX: fx,
          assetPrefix: 'assets/brandA',
        );
      case 'brandB':
        return FlavorConfig._(
          flavor: Flavor.brandB,
          name: 'BrandB',
          apiUrl: url,
          featureX: fx,
          assetPrefix: 'assets/brandB',
          primaryColor: Color(int.parse(c)),
        );
      case 'dev':
      default:
        return FlavorConfig._(
          flavor: Flavor.dev,
          name: 'Dev',
          apiUrl: url,
          featureX: fx,
          assetPrefix: 'assets/common',
          primaryColor: Color(int.parse(c)),
        );
    }
  }

  static FlavorConfig get instance {
    if (_instance == null) {
      throw StateError('FlavorConfig não inicializado');
    }
    return _instance!;
  }
}
