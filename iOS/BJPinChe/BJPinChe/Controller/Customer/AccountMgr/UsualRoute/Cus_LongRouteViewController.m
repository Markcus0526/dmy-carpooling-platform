//
//  Cus_LongRouteViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  设置日常路线 未完成  不再开发

#import "Cus_LongRouteViewController.h"

@interface Cus_LongRouteViewController ()

@end



@implementation Cus_LongRouteViewController

@synthesize selectedIndexPath;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	
	[self callGetRoutes];
}

- (void) updateUI
{
	[_tableView reloadData];
}

- (void) callGetRoutes
{
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	TEST_NETWORK_RETURN;

	// Call the GetUsualRoutes service routine.
	[[CommManager getCommMgr] accountSvcMgr].delegate = self;
	[[[CommManager getCommMgr] accountSvcMgr] GetUsualRoutes:[Common getUserID] type:ROUTETYPE_LONG devtoken:[Common getDeviceMacAddress]];
}

- (void) getUsualRoutesResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];

		mRouteArray = dataList;

		// refresh table
		[self updateUI];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}

- (void) callDelRoute : (long)route_id
{
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	TEST_NETWORK_RETURN;

	// Call the GetUsualRoutes service routine.
	[[CommManager getCommMgr] accountSvcMgr].delegate = self;
	[[[CommManager getCommMgr] accountSvcMgr] DelUsualRoute:[Common getUserID] route_id:route_id devtoken:[Common getDeviceMacAddress]];
}

- (void) delUsualRouteResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];
		
		// refresh table
		[self callGetRoutes];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Standard TableView delegates

////////////////////////////////////////////////////////////////////////////////////////////////////
- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	if (mRouteArray == nil) {
		return 0;
	}
	
	return mRouteArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return 67;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	static NSString *cellIdentifier = @"longroute";
	
	LongRouteCell * cell = (LongRouteCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
	//    cell.selectionStyle = UITableViewCellSelectionStyleNone;
	
	STRouteInfo * info = [mRouteArray objectAtIndex:indexPath.row];
	cell.delegate = self;
	cell.delDelegate = self;
	[cell initData:info parent:self];
	
	return cell;
	
}


#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (self.selectedIndexPath.row != indexPath.row) {
		[_tableView deselectRowAtIndexPath:indexPath animated:YES];
		[self resetSelectedCell];
	}
	if (self.selectedIndexPath.row == indexPath.row) {
		[_tableView deselectRowAtIndexPath:indexPath animated:YES];
		[self resetSelectedCell];
	}
}

-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
	if (self.selectedIndexPath) {
		[self resetSelectedCell];
	}
}

#pragma mark - RMSwipeTableViewCell BasketTableViewCell delegate method

-(void)swipeTableViewCellDidDelete:(LongRouteCell *)swipeTableViewCell
{
	NSIndexPath *indexPath = [_tableView indexPathForCell:swipeTableViewCell];

	// get current selected item
	int index = indexPath.row;
	STRouteInfo * item = [mRouteArray objectAtIndex:index];

	NSMutableArray * arrDelItems = [[NSMutableArray alloc] init];
	[arrDelItems addObject:item];
	
	[self callDelRoute:item.uid];
}


#pragma mark - RMSwipeTableViewCell delegate methods

-(void)swipeTableViewCellDidStartSwiping:(RMSwipeTableViewCell *)swipeTableViewCell {
	NSIndexPath *indexPathForCell = [_tableView indexPathForCell:swipeTableViewCell];
	if (self.selectedIndexPath.row != indexPathForCell.row) {
		[self resetSelectedCell];
	}
}

-(void)resetSelectedCell {
	LongRouteCell *cell = (LongRouteCell*)[_tableView cellForRowAtIndexPath:self.selectedIndexPath];
	[cell resetContentView];
	self.selectedIndexPath = nil;
	cell.selectionStyle = UITableViewCellSelectionStyleGray;
}

-(void)swipeTableViewCellWillResetState:(RMSwipeTableViewCell *)swipeTableViewCell fromPoint:(CGPoint)point animation:(RMSwipeTableViewCellAnimationType)animation velocity:(CGPoint)velocity {
	if (velocity.x <= -300) {
		selectedIndexPath = [_tableView indexPathForCell:swipeTableViewCell];
		swipeTableViewCell.shouldAnimateCellReset = NO;
		swipeTableViewCell.selectionStyle = UITableViewCellSelectionStyleNone;
		NSTimeInterval duration = MAX(-point.x / ABS(velocity.x), 0.10f);
		[UIView animateWithDuration:duration
							  delay:0
							options:UIViewAnimationOptionCurveLinear
						 animations:^{
							 swipeTableViewCell.contentView.frame = CGRectOffset(swipeTableViewCell.contentView.bounds, point.x - (ABS(velocity.x) / 150), 0);
						 }
						 completion:^(BOOL finished) {
							 [UIView animateWithDuration:duration
												   delay:0
												 options:UIViewAnimationOptionCurveEaseOut
											  animations:^{
												  swipeTableViewCell.contentView.frame = CGRectOffset(swipeTableViewCell.contentView.bounds, -BUTTON_THRESHOLD, 0);
											  }
											  completion:^(BOOL finished) {
											  }];
						 }];
	}
	// The below behaviour is not normal as of iOS 7 beta seed 1
	// for Messages.app, but it is for Mail.app.
	// The user has to pan/swipe with a certain amount of velocity
	// before the cell goes to delete-state. If the user just pans
	// above the threshold for the button but without enough velocity,
	// the cell will reset.
	// Mail.app will, however allow for the cell to reveal the button
	// even if the velocity isn't high, but the pan translation is
	// above the threshold. I am assuming it'll get more consistent
	// in later seed of the iOS 7 beta
	
	else if (velocity.x > -500 && point.x < -80) {
		self.selectedIndexPath = [_tableView indexPathForCell:swipeTableViewCell];
		swipeTableViewCell.shouldAnimateCellReset = NO;
		swipeTableViewCell.selectionStyle = UITableViewCellSelectionStyleNone;
		NSTimeInterval duration = MIN(-point.x / ABS(velocity.x), 0.15f);
		[UIView animateWithDuration:duration
						 animations:^{
							 swipeTableViewCell.contentView.frame = CGRectOffset(swipeTableViewCell.contentView.bounds, -120, 0);
						 }];
	}else
    {
        self.selectedIndexPath = [_tableView indexPathForCell:swipeTableViewCell];
        swipeTableViewCell.shouldAnimateCellReset = NO;
        swipeTableViewCell.selectionStyle = UITableViewCellSelectionStyleNone;
        NSTimeInterval duration = MIN(-point.x / ABS(velocity.x), 0.15f);
        [UIView animateWithDuration:duration
                         animations:^{
                             swipeTableViewCell.contentView.frame = CGRectOffset(swipeTableViewCell.contentView.bounds, 0, 0);
                         }];
    }
	
}



///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


@end
