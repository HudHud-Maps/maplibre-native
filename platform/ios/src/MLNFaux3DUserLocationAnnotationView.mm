#import "MLNFaux3DUserLocationAnnotationView.h"

#import "MLNMapView_Private.h"
#import "MLNMapViewDelegate.h"
#import "MLNUserLocation.h"
#import "MLNUserLocationHeadingIndicator.h"
#import "MLNUserLocationHeadingArrowLayer.h"
#import "MLNUserLocationHeadingBeamLayer.h"
#import "MLNLocationManager_Private.h"
#import "MLNUserLocationAnnotationViewStyle.h"

const CGFloat MLNUserLocationAnnotationDotSize = 22.0;
const CGFloat MLNUserLocationAnnotationHaloSize = 115.0;

const CGFloat MLNUserLocationAnnotationPuckSize = 45.0;
const CGFloat MLNUserLocationAnnotationArrowSize = MLNUserLocationAnnotationPuckSize * 0.5;

const CGFloat MLNUserLocationHeadingUpdateThreshold = 0.01;

const CGFloat MLNUserLocationApproximateZoomThreshold = 7.0;

@implementation MLNFaux3DUserLocationAnnotationView
{
    BOOL _puckModeActivated;
    BOOL _approximateModeActivated;

    CALayer *_puckDot;
    CAShapeLayer *_puckArrow;

    CALayer<MLNUserLocationHeadingIndicator> *_headingIndicatorLayer;
    CALayer *_accuracyRingLayer;
    CALayer *_dotBorderLayer;
    CALayer *_dotLayer;
    CALayer *_haloLayer;

    CALayer *_approximateLayer;

    CLLocationDirection _oldHeadingAccuracy;
    CLLocationAccuracy _oldHorizontalAccuracy;
    double _oldZoom;
    double _oldPitch;
}

- (CALayer *)hitTestLayer
{
    // Only the main dot should be interactive (i.e., exclude the accuracy ring and halo).
    return _dotBorderLayer ?: _puckDot;
}

- (void)update
{
    if (CGSizeEqualToSize(self.frame.size, CGSizeZero))
    {
        CGFloat frameSize = (self.mapView.userTrackingMode == MLNUserTrackingModeFollowWithCourse) ? MLNUserLocationAnnotationPuckSize : MLNUserLocationAnnotationDotSize;
        [self updateFrameWithSize:frameSize];
    }

    if (CLLocationCoordinate2DIsValid(self.userLocation.coordinate))
    {
        if (@available(iOS 14, *)) {
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 140000
            if (![self.mapView.locationManager respondsToSelector:@selector(accuracyAuthorization)] ||
                self.mapView.locationManager.accuracyAuthorization == CLAccuracyAuthorizationFullAccuracy) {
                [self drawPreciseLocationPuck];
            } else {
                [self drawApproximate];
                [self updatePitch];
            }
#endif
        } else {
            [self drawPreciseLocationPuck];
        }

    }


}

- (void)drawPreciseLocationPuck {
    if (_approximateModeActivated) {
        [_approximateLayer removeFromSuperlayer];
        _approximateLayer = nil;

        _approximateModeActivated = NO;
    }
    (self.mapView.userTrackingMode == MLNUserTrackingModeFollowWithCourse) ? [self drawPuck] : [self drawDot];
    [self updatePitch];
    _haloLayer.hidden = ! CLLocationCoordinate2DIsValid(self.mapView.userLocation.coordinate) || self.mapView.userLocation.location.horizontalAccuracy > 10;
}

- (void)setTintColor:(UIColor *)tintColor
{
    UIColor *puckArrowFillColor = tintColor;
    UIColor *puckArrowStrokeColor = tintColor;

    UIColor *approximateFillColor = tintColor;

    UIColor *accuracyFillColor = tintColor;
    UIColor *haloFillColor = tintColor;
    UIColor *dotFillColor = tintColor;
    UIColor *headingFillColor = tintColor;

    if ([self.mapView.delegate respondsToSelector:@selector(mapViewStyleForDefaultUserLocationAnnotationView:)]) {
        MLNUserLocationAnnotationViewStyle *style = [self.mapView.delegate mapViewStyleForDefaultUserLocationAnnotationView:self.mapView];

        puckArrowFillColor = style.puckArrowFillColor ? style.puckArrowFillColor : puckArrowFillColor;

        if (@available(iOS 14, *)) {
            approximateFillColor = style.approximateHaloFillColor ? style.approximateHaloFillColor : approximateFillColor;
        }

        haloFillColor = style.haloFillColor ? style.haloFillColor : haloFillColor;
        dotFillColor = style.puckFillColor ? style.puckFillColor : dotFillColor;
        headingFillColor = style.puckFillColor ? style.puckFillColor : headingFillColor;
    }

    if (_puckModeActivated)
    {
        _puckArrow.fillColor = [puckArrowFillColor CGColor];
        _puckArrow.strokeColor = [puckArrowStrokeColor CGColor];
    }
    else if (_approximateModeActivated)
    {
        _approximateLayer.backgroundColor = [approximateFillColor CGColor];
    }
    else
    {
        _accuracyRingLayer.backgroundColor = [accuracyFillColor CGColor];
        _haloLayer.backgroundColor = [haloFillColor CGColor];
        _dotLayer.backgroundColor = [dotFillColor CGColor];
        [_headingIndicatorLayer updateTintColor:[headingFillColor CGColor]];
    }

}

- (void)updatePitch
{
    if (self.mapView.camera.pitch != _oldPitch)
    {
        // disable implicit animation
        [CATransaction begin];
        [CATransaction setDisableActions:YES];

        CATransform3D t = CATransform3DRotate(CATransform3DIdentity, MLNRadiansFromDegrees(self.mapView.camera.pitch), 1.0, 0, 0);
        self.layer.sublayerTransform = t;

        [self updateFaux3DEffect];

        [CATransaction commit];

        _oldPitch = self.mapView.camera.pitch;
    }
}

- (void)updateFaux3DEffect
{
    CGFloat pitch = MLNRadiansFromDegrees(self.mapView.camera.pitch);

    if (_puckDot)
    {
        _puckDot.shadowOffset = CGSizeMake(0, fmaxf(pitch * 10.f, 1.f));
        _puckDot.shadowRadius = fmaxf(pitch * 5.f, 0.75f);
    }

    if (_dotBorderLayer)
    {
        _dotBorderLayer.shadowOffset = CGSizeMake(0.f, pitch * 10.f);
        _dotBorderLayer.shadowRadius = fmaxf(pitch * 5.f, 3.f);
    }

    if (_dotLayer)
    {
        _dotLayer.zPosition = pitch * 2.f;
    }
}

- (void)updateFrameWithSize:(CGFloat)size
{
    CGSize newSize = CGSizeMake(size, size);
    if (CGSizeEqualToSize(self.frame.size, newSize))
    {
        return;
    }

    // Update frame size, keeping the existing center point.
    CGPoint oldCenter = self.center;
    CGRect newFrame = self.frame;
    newFrame.size = newSize;
    [self setFrame:newFrame];
    [self setCenter:oldCenter];
}

- (void)drawPuck
{
    if ( ! _puckModeActivated)
    {
        self.layer.sublayers = nil;

        _headingIndicatorLayer = nil;
        _accuracyRingLayer = nil;
        _haloLayer = nil;
        _dotBorderLayer = nil;
        _dotLayer = nil;

        [self updateFrameWithSize:MLNUserLocationAnnotationPuckSize];
    }

    UIColor *arrowColor = self.mapView.tintColor;
    UIColor *puckShadowColor = UIColor.blackColor;
    CGFloat shadowOpacity = 0.25;


    if ([self.mapView.delegate respondsToSelector:@selector(mapViewStyleForDefaultUserLocationAnnotationView:)]) {
        MLNUserLocationAnnotationViewStyle *style = [self.mapView.delegate mapViewStyleForDefaultUserLocationAnnotationView:self.mapView];
        arrowColor = style.puckArrowFillColor ? style.puckArrowFillColor : arrowColor;
        puckShadowColor = style.puckShadowColor ? style.puckShadowColor : puckShadowColor;
        shadowOpacity = style.puckShadowOpacity;
    }

    // background dot (white with black shadow)
    //
    if ( ! _puckDot)
    {
        _puckDot = [self circleLayerWithSize:MLNUserLocationAnnotationPuckSize];
        _puckDot.backgroundColor = [[UIColor whiteColor] CGColor];
        _puckDot.shadowColor = [puckShadowColor CGColor];
        _puckDot.shadowOpacity = shadowOpacity;
        _puckDot.shadowPath = [[UIBezierPath bezierPathWithOvalInRect:_puckDot.bounds] CGPath];

        if (self.mapView.camera.pitch)
        {
            [self updateFaux3DEffect];
        }
        else
        {
            _puckDot.shadowOffset = CGSizeMake(0, 1);
            _puckDot.shadowRadius = 0.75;
        }

        [self.layer addSublayer:_puckDot];
    }

    // arrow
    //
    if ( ! _puckArrow)
    {
        _puckArrow = [CAShapeLayer layer];
        _puckArrow.path = [[self puckArrow] CGPath];
        _puckArrow.fillColor = [arrowColor CGColor];
        _puckArrow.bounds = CGRectMake(0, 0, round(MLNUserLocationAnnotationArrowSize), round(MLNUserLocationAnnotationArrowSize));
        _puckArrow.position = CGPointMake(CGRectGetMidX(super.bounds), CGRectGetMidY(super.bounds));
        _puckArrow.shouldRasterize = YES;
        _puckArrow.rasterizationScale = [UIScreen mainScreen].scale;
        _puckArrow.drawsAsynchronously = YES;

        _puckArrow.lineJoin = @"round";
        _puckArrow.lineWidth = 1.f;
        _puckArrow.strokeColor = _puckArrow.fillColor;

        [self.layer addSublayer:_puckArrow];
    }
    else if (!CGColorEqualToColor(_puckArrow.fillColor, [arrowColor CGColor]))
    {
        _puckArrow.fillColor = [arrowColor CGColor];
        _puckArrow.strokeColor = [arrowColor CGColor];
    }
    if (self.userLocation.location.course >= 0)
    {
        _puckArrow.affineTransform = CGAffineTransformRotate(CGAffineTransformIdentity, -MLNRadiansFromDegrees(self.mapView.direction - self.userLocation.location.course));
    }

    if ( ! _puckModeActivated)
    {
        _puckModeActivated = YES;

        [self updateFaux3DEffect];
    }
}

- (UIBezierPath *)puckArrow
{
    CGFloat max = MLNUserLocationAnnotationArrowSize;

    UIBezierPath *bezierPath = UIBezierPath.bezierPath;
    [bezierPath moveToPoint:    CGPointMake(max * 0.5, 0)];
    [bezierPath addLineToPoint: CGPointMake(max * 0.1, max)];
    [bezierPath addLineToPoint: CGPointMake(max * 0.5, max * 0.65)];
    [bezierPath addLineToPoint: CGPointMake(max * 0.9, max)];
    [bezierPath addLineToPoint: CGPointMake(max * 0.5, 0)];
    [bezierPath closePath];

    return bezierPath;
}

- (void)drawDot
{
    if (_puckModeActivated)
    {
        self.layer.sublayers = nil;

        _puckDot = nil;
        _puckArrow = nil;

        [self updateFrameWithSize:MLNUserLocationAnnotationDotSize];
    }

    UIColor *haloColor = self.mapView.tintColor;
    UIColor *puckBackgroundColor = self.mapView.tintColor;
    UIColor *puckShadowColor = UIColor.blackColor;
    CGFloat shadowOpacity = 0.25;


    if ([self.mapView.delegate respondsToSelector:@selector(mapViewStyleForDefaultUserLocationAnnotationView:)]) {
        MLNUserLocationAnnotationViewStyle *style = [self.mapView.delegate mapViewStyleForDefaultUserLocationAnnotationView:self.mapView];
        haloColor = style.haloFillColor ? style.haloFillColor : haloColor;
        puckBackgroundColor = style.puckFillColor ? style.puckFillColor : puckBackgroundColor;
        puckShadowColor = style.puckShadowColor ? style.puckShadowColor : puckShadowColor;
        shadowOpacity = style.puckShadowOpacity;
    }

    // heading indicator (tinted, beam or arrow)
    //
    BOOL headingTrackingModeEnabled = self.mapView.userTrackingMode == MLNUserTrackingModeFollowWithHeading;
    BOOL showHeadingIndicator = self.mapView.showsUserHeadingIndicator || headingTrackingModeEnabled;

    if (showHeadingIndicator)
    {
        _headingIndicatorLayer.hidden = NO;
        CLLocationDirection headingAccuracy = self.userLocation.heading.headingAccuracy;

        if (([_headingIndicatorLayer isMemberOfClass:[MLNUserLocationHeadingBeamLayer class]] && ! headingTrackingModeEnabled) ||
            ([_headingIndicatorLayer isMemberOfClass:[MLNUserLocationHeadingArrowLayer class]] && headingTrackingModeEnabled))
        {
            [_headingIndicatorLayer removeFromSuperlayer];
            _headingIndicatorLayer = nil;
            _oldHeadingAccuracy = -1;
        }

        if ( ! _headingIndicatorLayer && headingAccuracy)
        {
            if (headingTrackingModeEnabled)
            {
                _headingIndicatorLayer = [[MLNUserLocationHeadingBeamLayer alloc] initWithUserLocationAnnotationView:self];
                [_headingIndicatorLayer updateTintColor:[haloColor CGColor]];
                [self.layer insertSublayer:_headingIndicatorLayer below:_dotBorderLayer];
            }
            else
            {
                _headingIndicatorLayer = [[MLNUserLocationHeadingArrowLayer alloc] initWithUserLocationAnnotationView:self];
                [_headingIndicatorLayer updateTintColor:[puckBackgroundColor CGColor]];
                [self.layer addSublayer:_headingIndicatorLayer];
                _headingIndicatorLayer.zPosition = 1;
            }
        }

        if (_oldHeadingAccuracy != headingAccuracy)
        {
            [_headingIndicatorLayer updateHeadingAccuracy:headingAccuracy];
            _oldHeadingAccuracy = headingAccuracy;
        }

        CLLocationDirection headingDirection = (self.userLocation.heading.trueHeading >= 0 ? self.userLocation.heading.trueHeading : self.userLocation.heading.magneticHeading);
        if (headingDirection >= 0)
        {
            CGFloat rotation = -MLNRadiansFromDegrees(self.mapView.direction - headingDirection);

            // Don't rotate if the change is imperceptible.
            if (fabs(rotation) > MLNUserLocationHeadingUpdateThreshold)
            {
                [CATransaction begin];
                [CATransaction setDisableActions:YES];

                _headingIndicatorLayer.affineTransform = CGAffineTransformRotate(CGAffineTransformIdentity, rotation);

                [CATransaction commit];
            }
        }
    }
    else
    {
        [_headingIndicatorLayer removeFromSuperlayer];
        _headingIndicatorLayer = nil;
    }

    // update accuracy ring (if zoom or horizontal accuracy have changed)
    //
    if (_accuracyRingLayer && (_oldZoom != self.mapView.zoomLevel || _oldHorizontalAccuracy != self.userLocation.location.horizontalAccuracy))
    {
        CGFloat accuracyRingSize = [self calculateAccuracyRingSize: self.mapView.zoomLevel];

        // only show the accuracy ring if it won't be obscured by the location dot
        if (accuracyRingSize > MLNUserLocationAnnotationDotSize + 15)
        {
            _accuracyRingLayer.hidden = NO;

            // disable implicit animation of the accuracy ring, unless triggered by a change in accuracy
            BOOL shouldDisableActions = _oldHorizontalAccuracy == self.userLocation.location.horizontalAccuracy;

            [CATransaction begin];
            [CATransaction setDisableActions:shouldDisableActions];

            _accuracyRingLayer.bounds = CGRectMake(0, 0, accuracyRingSize, accuracyRingSize);
            _accuracyRingLayer.cornerRadius = accuracyRingSize / 2.0;

            // match the halo to the accuracy ring
            _haloLayer.bounds = _accuracyRingLayer.bounds;
            _haloLayer.cornerRadius = _accuracyRingLayer.cornerRadius;
            _haloLayer.shouldRasterize = NO;

            [CATransaction commit];
        }
        else
        {
            _accuracyRingLayer.hidden = YES;

            _haloLayer.bounds = CGRectMake(0, 0, MLNUserLocationAnnotationHaloSize, MLNUserLocationAnnotationHaloSize);
            _haloLayer.cornerRadius = MLNUserLocationAnnotationHaloSize / 2.0;
            _haloLayer.shouldRasterize = YES;
            _haloLayer.rasterizationScale = [UIScreen mainScreen].scale;
        }

        // store accuracy and zoom so we're not redrawing unchanged location updates
        _oldHorizontalAccuracy = self.userLocation.location.horizontalAccuracy;
        _oldZoom = self.mapView.zoomLevel;
    }

    // accuracy ring (circular, tinted, mostly-transparent)
    //
    if ( ! _accuracyRingLayer && self.userLocation.location.horizontalAccuracy)
    {
        CGFloat accuracyRingSize = [self calculateAccuracyRingSize: self.mapView.zoomLevel];
        _accuracyRingLayer = [self circleLayerWithSize:accuracyRingSize];
        _accuracyRingLayer.backgroundColor = [self.mapView.tintColor CGColor];
        _accuracyRingLayer.opacity = 0.1;
        _accuracyRingLayer.shouldRasterize = NO;
        _accuracyRingLayer.allowsGroupOpacity = NO;

        [self.layer addSublayer:_accuracyRingLayer];
    }

    // expanding sonar-like pulse (circular, tinted, fades out)
    //
    if ( ! _haloLayer)
    {
        _haloLayer = [self circleLayerWithSize:MLNUserLocationAnnotationHaloSize];
        _haloLayer.backgroundColor = [haloColor CGColor];
        _haloLayer.allowsGroupOpacity = NO;
        _haloLayer.zPosition = -0.1f;

        // set defaults for the animations
        CAAnimationGroup *animationGroup = [self loopingAnimationGroupWithDuration:3.0];

        // scale out radially with initial acceleration
        CAKeyframeAnimation *boundsAnimation = [CAKeyframeAnimation animationWithKeyPath:@"transform.scale.xy"];
        boundsAnimation.values = @[@0, @0.35, @1];
        boundsAnimation.keyTimes = @[@0, @0.2, @1];

        // go transparent as scaled out, start semi-opaque
        CAKeyframeAnimation *opacityAnimation = [CAKeyframeAnimation animationWithKeyPath:@"opacity"];
        opacityAnimation.values = @[@0.4, @0.4, @0];
        opacityAnimation.keyTimes = @[@0, @0.2, @1];

        animationGroup.animations = @[boundsAnimation, opacityAnimation];

        [_haloLayer addAnimation:animationGroup forKey:@"animateTransformAndOpacity"];

        [self.layer addSublayer:_haloLayer];
    }
    else if (!CGColorEqualToColor(_haloLayer.backgroundColor, [haloColor CGColor]))
    {
        _haloLayer.backgroundColor = [haloColor CGColor];
    }

    // background dot (white with black shadow)
    //
    if ( ! _dotBorderLayer)
    {
        _dotBorderLayer = [self circleLayerWithSize:MLNUserLocationAnnotationDotSize];
        _dotBorderLayer.backgroundColor = [[UIColor whiteColor] CGColor];
        _dotBorderLayer.shadowColor = [puckShadowColor CGColor];
        _dotBorderLayer.shadowOpacity = shadowOpacity;
        _dotBorderLayer.shadowPath = [[UIBezierPath bezierPathWithOvalInRect:_dotBorderLayer.bounds] CGPath];

        if (self.mapView.camera.pitch)
        {
            [self updateFaux3DEffect];
        }
        else
        {
            _dotBorderLayer.shadowOffset = CGSizeMake(0, 0);
            _dotBorderLayer.shadowRadius = 3;
        }

        [self.layer addSublayer:_dotBorderLayer];
    }

    // inner dot (pulsing, tinted)
    //
    if ( ! _dotLayer)
    {
        _dotLayer = [self circleLayerWithSize:MLNUserLocationAnnotationDotSize * 0.75];
        _dotLayer.backgroundColor = [puckBackgroundColor CGColor];

        // set defaults for the animations
        CAAnimationGroup *animationGroup = [self loopingAnimationGroupWithDuration:1.5];
        animationGroup.autoreverses = YES;
        animationGroup.fillMode = kCAFillModeBoth;

        // scale the dot up and down
        CABasicAnimation *pulseAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale.xy"];
        pulseAnimation.fromValue = @0.8;
        pulseAnimation.toValue = @1;

        // fade opacity in and out, subtly
        CABasicAnimation *opacityAnimation = [CABasicAnimation animationWithKeyPath:@"opacity"];
        opacityAnimation.fromValue = @0.8;
        opacityAnimation.toValue = @1;

        animationGroup.animations = @[pulseAnimation, opacityAnimation];

        [_dotLayer addAnimation:animationGroup forKey:@"animateTransformAndOpacity"];

        [self.layer addSublayer:_dotLayer];
    }
    else if (!CGColorEqualToColor(_dotLayer.backgroundColor, [puckBackgroundColor CGColor]))
    {
        _dotLayer.backgroundColor = [puckBackgroundColor CGColor];
    }

    if (_puckModeActivated)
    {
        _puckModeActivated = NO;

        [self updateFaux3DEffect];
    }

}

- (void)drawApproximate
{

    if ( ! _approximateModeActivated)
    {
        self.layer.sublayers = nil;

        _headingIndicatorLayer = nil;
        _dotBorderLayer = nil;
        _dotLayer = nil;
        _accuracyRingLayer = nil;
        _haloLayer = nil;
        _puckDot = nil;
        _puckArrow = nil;

        _approximateModeActivated = YES;
    }

    UIColor *backgroundColor = self.mapView.tintColor;
    UIColor *strokeColor = UIColor.blackColor;
    CGFloat borderSize = 2.0;
    CGFloat opacity = 0.25;

    if ([self.mapView.delegate respondsToSelector:@selector(mapViewStyleForDefaultUserLocationAnnotationView:)]) {
        MLNUserLocationAnnotationViewStyle *style = [self.mapView.delegate mapViewStyleForDefaultUserLocationAnnotationView:self.mapView];
        if (@available(iOS 14, *)) {
            backgroundColor = style.approximateHaloFillColor ? style.approximateHaloFillColor : backgroundColor;
            strokeColor = style.approximateHaloBorderColor ? style.approximateHaloBorderColor : strokeColor;
            opacity = style.approximateHaloOpacity;
            borderSize = style.approximateHaloBorderWidth;
        }
    }

    // approximate ring
    if ( ! _approximateLayer && self.userLocation.location.horizontalAccuracy)
    {
        CGFloat accuracyRingSize = [self calculateAccuracyRingSize: MAX(self.mapView.zoomLevel, MLNUserLocationApproximateZoomThreshold)];
        _approximateLayer = [self circleLayerWithSize:accuracyRingSize];
        _approximateLayer.backgroundColor = [backgroundColor CGColor];
        _approximateLayer.opacity = opacity;
        _approximateLayer.shouldRasterize = NO;
        _approximateLayer.allowsGroupOpacity = NO;
        _approximateLayer.borderWidth = borderSize;
        _approximateLayer.borderColor = [strokeColor CGColor];

        [self.layer addSublayer:_approximateLayer];
    }

    // update approximate ring (if zoom or horizontal accuracy have changed)
    if (_approximateLayer && (_oldZoom != self.mapView.zoomLevel || _oldHorizontalAccuracy != self.userLocation.location.horizontalAccuracy))
    {
        if (self.mapView.zoomLevel < MLNUserLocationApproximateZoomThreshold) {
            borderSize = 1.0;
        }
        _approximateLayer.borderWidth = borderSize;

        if (self.mapView.zoomLevel >= MLNUserLocationApproximateZoomThreshold) {
            CGFloat accuracyRingSize = [self calculateAccuracyRingSize: self.mapView.zoomLevel];

            _approximateLayer.hidden = NO;

            // disable implicit animation of the accuracy ring, unless triggered by a change in accuracy
            BOOL shouldDisableActions = _oldHorizontalAccuracy == self.userLocation.location.horizontalAccuracy;

            [CATransaction begin];
            [CATransaction setDisableActions:shouldDisableActions];

            _approximateLayer.bounds = CGRectMake(0, 0, accuracyRingSize, accuracyRingSize);
            _approximateLayer.cornerRadius = accuracyRingSize / 2.0;

            [CATransaction commit];
        }

        // store accuracy and zoom so we're not redrawing unchanged location updates
        _oldHorizontalAccuracy = self.userLocation.location.horizontalAccuracy;
        _oldZoom = self.mapView.zoomLevel;
    }
}

- (CALayer *)circleLayerWithSize:(CGFloat)layerSize
{
    layerSize = round(layerSize);

    CALayer *circleLayer = [CALayer layer];
    circleLayer.bounds = CGRectMake(0, 0, layerSize, layerSize);
    circleLayer.position = CGPointMake(CGRectGetMidX(super.bounds), CGRectGetMidY(super.bounds));
    circleLayer.cornerRadius = layerSize / 2.0;
    circleLayer.shouldRasterize = YES;
    circleLayer.rasterizationScale = [UIScreen mainScreen].scale;
    circleLayer.drawsAsynchronously = YES;

    return circleLayer;
}

- (CAAnimationGroup *)loopingAnimationGroupWithDuration:(CGFloat)animationDuration
{
    CAAnimationGroup *animationGroup = [CAAnimationGroup animation];
    animationGroup.duration = animationDuration;
    animationGroup.repeatCount = INFINITY;
    animationGroup.removedOnCompletion = NO;
    animationGroup.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault];

    return animationGroup;
}

- (CGFloat)calculateAccuracyRingSize:(double)zoomLevel
{
    // diameter in screen points
    return round(self.userLocation.location.horizontalAccuracy / [self.mapView metersPerPointAtLatitude:self.userLocation.coordinate.latitude zoomLevel:zoomLevel] * 2.0);
}

@end
