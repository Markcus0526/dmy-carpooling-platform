//
//  Cus_driverInfoViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_driverInfoViewController.h"
#import "DriverEvalTableViewCell.h"
@interface Cus_driverInfoViewController ()
@property (nonatomic,strong)NSMutableArray *dataArray;
@end

@implementation Cus_driverInfoViewController
{
    STDriverInfo * _driverInfo;
   // NSMutableArray * _dataArray;
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(NSMutableArray *)dataArray
{
 if(_dataArray ==nil)
 {
     _dataArray =[[NSMutableArray alloc]init];
 }
    return _dataArray;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self createDataSource];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)OnBackClick:(id)sender {
   // BACK_VIEW;
    
    if (self.navigationController ==nil) {
        BACK_VIEW;
    }else
    {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(void)createDataSource
{
	TEST_NETWORK_RETURN;

	[SVProgressHUD show];
  //  _dataArray = [[NSMutableArray alloc]init];
    OrderSvcMgr *orderSvcMgr = [[CommManager getCommMgr] orderSvcMgr];
    orderSvcMgr.delegate = self;
    [orderSvcMgr GetDriverInfo:[Common getUserID] DriverId:self.driverid DevToken:[Common getDeviceMacAddress]];
}

-(void)getDriverInfoResult:(NSString *)result DriverInfo:(STDriverInfo *)driver_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        [SVProgressHUD dismiss];
        _driverInfo = driver_info;
        [self refreshView];
        
    }else{
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

-(void)refreshView
{
    [self.driverImg setImageWithURL:[NSURL URLWithString:_driverInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    [self.carImg setImageWithURL:[NSURL URLWithString:_driverInfo.carinfo.carimg] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    self.career.text = [NSString stringWithFormat:@"驾龄 %d年",_driverInfo.drv_career];
    self.evGoodRate.text = _driverInfo.goodeval_rate_desc;
    self.carPoolTimes.text = _driverInfo.carpool_count_desc;
    if (_driverInfo.sex == 0) {
        [self.gender setImage:[UIImage imageNamed:@"icon_male.png"]];
         self.age.textColor = [UIColor colorWithRed:0.30f green:0.71f blue:0.49f alpha:1.00f];
    }else{
        [self.gender setImage:[UIImage imageNamed:@"icon_female.png"]];
        self.age.textColor = [UIColor colorWithRed:0.98f green:0.44f blue:0.45f alpha:1.00f];
    }
    self.name.text = _driverInfo.name;
    [self.carStyle setImage:[UIImage imageNamed:[NSString stringWithFormat:@"cartype%d_active.png",_driverInfo.carinfo.style]]];
    self.brand.text = _driverInfo.carinfo.brand;
    self.type.text = _driverInfo.carinfo.type;
    self.color.text = _driverInfo.carinfo.color;
    _dataArray = _driverInfo.evals;
    [self.tableview reloadData];
}

#pragma mark - tableView 协议实现

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cellName = @"DriverEvalCell";
    DriverEvalTableViewCell * LongWayCell = [_tableview dequeueReusableCellWithIdentifier:cellName];
    if (!LongWayCell) {
        LongWayCell = [[[NSBundle mainBundle]loadNibNamed:@"DriverEvalTableViewCell" owner:self options:nil]lastObject];
    }
    LongWayCell.model = _dataArray[indexPath.row];
    
    return LongWayCell;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}









@end
